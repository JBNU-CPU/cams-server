package com.cpu.cams.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final long DEFAULT_TIMEOUT = 60L * 60L * 1000L; // 1 hour
    private static final String EVENT_NAME = "notify";

    private final EmitterRepository emitterRepo;
    private final NotificationRepository notificationRepo;

    /** 구독: 미읽음 먼저 쏜 다음 연결 유지 */
    @Transactional(readOnly = true)
    public SseEmitter subscribe(Long userId, String lastEventId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        String emitterId = UUID.randomUUID().toString();
        emitterRepo.save(userId, emitterId, emitter);

        emitter.onCompletion(() -> emitterRepo.remove(userId, emitterId));
        emitter.onTimeout(() -> emitterRepo.remove(userId, emitterId));
        emitter.onError(e -> emitterRepo.remove(userId, emitterId));

        // 0) 첫 핸드셰이크
        try {
            emitter.send(SseEmitter.event()
                    .id("init-" + System.currentTimeMillis())
                    .name("init")
                    .data("connected"));
        } catch (IOException e) {
            emitterRepo.remove(userId, emitterId);
            emitter.complete();
            return emitter;
        }

        // 1) 미읽음 알림 선푸시
        List<Notification> unread = notificationRepo
                .findAllByUserIdAndReadAtIsNullOrderByCreatedAtAsc(userId);
        for (Notification n : unread) {
            try {
                emitter.send(SseEmitter.event()
                        .id(n.getId().toString())
                        .name(EVENT_NAME)
                        .data(NotificationResponse.from(n)));
            } catch (IOException e) {
                log.warn("Failed to send unread notification id={} to user={}", n.getId(), userId);
            }
        }

        // lastEventId 기반 재전송은 여기서 필요 시 구현 (옵션)

        return emitter;
    }

    /** 저장 + 실시간 푸시 */
    @Transactional
    public NotificationResponse createAndSend(Long userId, NotificationPayload payload) {
        Notification saved = notificationRepo.save(
                Notification.create(userId, payload.type(), payload.message(), payload.link())
        );

        // 실시간 푸시
        var emitters = emitterRepo.getAll(userId);
        emitters.forEach((id, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                        .id(saved.getId().toString())
                        .name(EVENT_NAME)
                        .data(NotificationResponse.from(saved)));
            } catch (IOException e) {
                emitter.complete();
                emitterRepo.remove(userId, id);
            }
        });

        return NotificationResponse.from(saved);
    }

    /** 읽음 처리 */
    @Transactional
    public void markRead(Long userId, Long notificationId) {
        Notification n = notificationRepo.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("notification not found"));
        if (!n.getUserId().equals(userId)) {
            throw new IllegalStateException("owner mismatch");
        }
        if (n.isUnread()) n.markRead();
    }

    // 선택: heartbeat
    @Scheduled(fixedRate = 25_000)
    public void heartbeatAll() {
        // 필요 시 전체 사용자 순회하여 가벼운 신호 전송 (규모 크면 비활성/샤딩 권장)
    }
}
