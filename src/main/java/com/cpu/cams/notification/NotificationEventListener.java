package com.cpu.cams.notification;

import com.cpu.cams.announcement.service.AnnouncementService;
import com.cpu.cams.notification.repository.EmitterRepository;
import com.cpu.cams.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationService notificationService;
    private final EmitterRepository emitterRepository;

    @TransactionalEventListener // 트랜잭션이 성공적으로 커밋될 때만 이벤트 수신
    public void handleNotificationEvent(AnnouncementService.NotificationEvent event) {
        // 1. DB에 알림 저장
        notificationService.createNotification(event.username(), event.message());

        // 2. 해당 사용자에게 실시간 알림 전송
        SseEmitter emitter = emitterRepository.get(event.username());
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(event.message()));

                // **참고**: 여기서 isSent=true로 변경하는 로직을 추가할 수 있습니다.
                // notificationService.markAsSent(notification.getId());
            } catch (IOException e) {
                // 전송 실패 시 emitter 제거
                emitterRepository.deleteByUsername(event.username());
            }
        }
    }
}
