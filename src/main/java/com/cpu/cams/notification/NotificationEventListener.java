package com.cpu.cams.notification;

import com.cpu.cams.announcement.service.AnnouncementService;
import com.cpu.cams.notification.repository.EmitterRepository;
import com.cpu.cams.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {

    private final NotificationService notificationService;
    private final EmitterRepository emitterRepository;

    @TransactionalEventListener // 트랜잭션이 성공적으로 커밋될 때만 이벤트 수신
    public void handleNotificationEvent(AnnouncementService.NotificationEvent event) {

        System.out.println("Notification Event Received");

        // 1. 알림을 DB에 저장 (오프라인 사용자를 위해)
        notificationService.createNotification(event.username(), event.message());
        System.out.println("event.username() = " + event.username());

        // 2. 현재 접속 중인 사용자인지 확인
        SseEmitter emitter = emitterRepository.get(event.username());
        if (emitter == null) {
            // 사용자가 오프라인 상태이면 아무것도 하지 않음
            System.out.println("emitter is null");
            return;
        }

        System.out.println("emitter = " + emitter);

        // 3. 온라인 사용자에게 실시간 알림 전송
        try {
            emitter.send(SseEmitter.event()
                    .name("notification") // 프론트엔드에서 받을 이벤트 이름
                    .data(event.message()));
        } catch (Exception e) {
            // 전송 중 에러 발생 시 Emitter 제거
            emitterRepository.deleteByUsername(event.username());
            log.error("Failed to send notification to user {}: {}", event.username(), e.getMessage());
        }
    }
}
