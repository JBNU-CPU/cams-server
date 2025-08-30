package com.cpu.cams.notification.controller;

import com.cpu.cams.member.dto.response.CustomUserDetails;
import com.cpu.cams.notification.entity.Notification;
import com.cpu.cams.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    @GetMapping("/stream")
    public SseEmitter streamNotifications(@AuthenticationPrincipal CustomUserDetails userDetails) {

        SseEmitter emitter = new SseEmitter(60000L); // 60초 타임아웃 설정
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));

        emitter.onTimeout(() -> {
            emitters.remove(emitter);
            emitter.complete();
        });

        emitter.onError((e) -> {
            emitters.remove(emitter);
            emitter.completeWithError(e);
        });

        // 사용자 인증 확인
        if (userDetails == null) {
            emitter.complete();
            return emitter;
        }

        String username = userDetails.getUsername();

        // 새로운 쓰레드에서 알림을 전송
        new Thread(() -> {
            try {
                while (true) {
                    if (emitters.contains(emitter)) {
                        List<Notification> notifications = notificationService.getUnreadNotificationsByUsername(username);

                        if (!notifications.isEmpty()) {
                            for (Notification notification : notifications) {
                                if (!notification.getIsSent()) { // 알림이 전송되지 않았는지 확인
                                    emitter.send(SseEmitter.event()
                                            .name("notification")
                                            .data(notification.getMessage()));

                                    // 알림을 읽음 상태로 업데이트 및 전송됨 상태로 설정
                                    notificationService.markAsRead(notification.getId());
                                }
                            }
                        }
                    } else {
                        break; // emitter가 이미 완료된 경우 반복문 종료
                    }
                    Thread.sleep(10000); // 10초마다 알림을 체크
                }
            } catch (Exception e) {
                System.out.println("e = " + e);
                // emitter.completeWithError(e);
            }
        }).start();
        return emitter;
    }
}
