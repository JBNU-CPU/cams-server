package com.cpu.cams.test;

import com.cpu.cams.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final long DEFAULT_TIMEOUT = 60L * 60L * 1000L; // 1 hour
    private static final String EVENT_NAME = "notify";
    private static final int BATCH_SIZE = 500; // 인원 많으면 값 키우세요

    private final EmitterRepository emitterRepo;
    private final NotificationRepository notificationRepo;
    private final MemberRepository memberRepository;

    /** 구독: 미읽음 먼저 쏜 다음 연결 유지 */
    @Transactional(readOnly = true)
    public SseEmitter subscribe(Long userId, String lastEventId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        String emitterId = UUID.randomUUID().toString();
        emitterRepo.save(userId, emitterId, emitter);

        // 클린업
        emitter.onCompletion(() -> emitterRepo.remove(userId, emitterId));
        emitter.onTimeout(() -> emitterRepo.remove(userId, emitterId));
        emitter.onError(e -> emitterRepo.remove(userId, emitterId));

        // 0) 첫 핸드셰이크
        try {
            emitter.send(SseEmitter.event()
//                    .id("init-" + System.currentTimeMillis())
                    .name("init")
                    .data("connected"));
        } catch (IOException e) {
            emitterRepo.remove(userId, emitterId);
            emitter.complete();
            return emitter;
        }

        // 1) lastEventId 이후 “세션 상 놓친 것” (read 여부 무관)
        List<Notification> missed = List.of();
        if (lastEventId != null && lastEventId.matches("\\d+")) {
            long lastId = Long.parseLong(lastEventId);
            missed = notificationRepo.findAllByUserIdAndIdGreaterThanOrderByIdAsc(userId, lastId);
        }

        // 2) 미읽음(backlog)
        List<Notification> unread =
                notificationRepo.findAllByUserIdAndReadAtIsNullOrderByCreatedAtAsc(userId);

        // 3) 합치고 id 오름차순 + 중복 제거
        var merged = java.util.stream.Stream.concat(missed.stream(), unread.stream())
                .collect(java.util.stream.Collectors.toMap(
                        Notification::getId,
                        n -> n,
                        (a,b) -> a, // 동일 id면 한 번만
                        java.util.TreeMap::new // key(=id) 오름차순 유지
                ))
                .values();

        // 4) 전송
        for (Notification n : merged) {
            try {
                emitter.send(SseEmitter.event()
                        .id(n.getId().toString())
                        .name(EVENT_NAME)
                        .data(NotificationResponse.from(n)));
            } catch (IOException ignore) {}
        }


//        // 1) 미읽음 알림 선푸시
//        List<Notification> unread = notificationRepo
//                .findAllByUserIdAndReadAtIsNullOrderByCreatedAtAsc(userId);
//        for (Notification n : unread) {
//            try { // 읽지않은 알림들 보내주기
//                emitter.send(SseEmitter.event()
//                        .id(n.getId().toString())
//                        .name(EVENT_NAME)
//                        .data(NotificationResponse.from(n)));
//            } catch (IOException e) {
//                log.warn("Failed to send unread notification id={} to user={}", n.getId(), userId);
//            }
//        }


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

    @Transactional
    public BroadcastResult broadcastToAll(NotificationPayload payload) {
        var allUserIds = memberRepository.findAllIds();
        if (allUserIds.isEmpty()) return new BroadcastResult(0, 0, 0);

        int inserted = 0;
        int pushed = 0;

        for (int i = 0; i < allUserIds.size(); i += BATCH_SIZE) {
            int to = Math.min(i + BATCH_SIZE, allUserIds.size());
            var chunk = allUserIds.subList(i, to);

            // 1) DB 일괄 저장 (오프라인 사용자를 위한 미읽음 적재)
            var toSave = new ArrayList<Notification>(chunk.size());
            for (Long uid : chunk) {
                toSave.add(Notification.create(uid, payload.type(), payload.message(), payload.link()));
            }
            var saved = notificationRepo.saveAll(toSave);
            inserted += saved.size();

            // 2) 온라인 유저(이미 emitter 열려있는 사람)에게 즉시 푸시
            Map<Long, List<Notification>> byUser =
                    saved.stream().collect(Collectors.groupingBy(Notification::getUserId));

            for (var entry : byUser.entrySet()) {
                Long userId = entry.getKey();
                var emitters = emitterRepo.getAll(userId);
                if (emitters.isEmpty()) continue;

                for (Notification n : entry.getValue()) {
                    for (var e : emitters.entrySet()) {
                        String emitterId = e.getKey();
                        SseEmitter emitter = e.getValue();
                        try {
                            emitter.send(SseEmitter.event()
                                    .id(n.getId().toString())
                                    .name(EVENT_NAME)
                                    .data(NotificationResponse.from(n)));
                            pushed++;
                        } catch (IOException ex) {
                            emitter.complete();
                            emitterRepo.remove(userId, emitterId);
                        }
                    }
                }
            }
        }

        return new BroadcastResult(allUserIds.size(), inserted, pushed);
    }


    // 선택: heartbeat
    @Scheduled(fixedRate = 25_000)
    public void heartbeatAll() {
        // 필요 시 전체 사용자 순회하여 가벼운 신호 전송 (규모 크면 비활성/샤딩 권장)
    }
}
