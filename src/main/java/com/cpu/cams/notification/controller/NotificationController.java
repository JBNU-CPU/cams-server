package com.cpu.cams.notification.controller;

import com.cpu.cams.member.dto.response.CustomUserDetails;
import com.cpu.cams.notification.entity.Notification;
import com.cpu.cams.notification.repository.EmitterRepository;
import com.cpu.cams.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;
    private final EmitterRepository emitterRepository;

    // 사용자별 emitter 관리
    private final ConcurrentMap<String, SseEmitter> emittersByUser = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    @GetMapping(value = "/stream")
    public SseEmitter streamNotifications(@AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return null; // 예외 처리 또는 적절한 응답
        }
        String username = userDetails.getUsername();
        SseEmitter emitter = new SseEmitter(60 * 1000L); // 1분

        // 기존 emitter가 있다면 삭제하고 새로 생성
        emitterRepository.deleteByUsername(username);
        emitterRepository.save(username, emitter);

        // 연결 종료 시 리포지토리에서 제거
        emitter.onCompletion(() -> emitterRepository.deleteByUsername(username));
        emitter.onTimeout(() -> emitterRepository.deleteByUsername(username));
        emitter.onError((e) -> emitterRepository.deleteByUsername(username));

        try {
            // 최초 연결 시 더미 데이터 전송 (503 Service Unavailable 방지)
            emitter.send(SseEmitter.event().name("connect").data("SSE connected for user: " + username));
        } catch (IOException e) {
            // 에러 발생 시 emitter 제거 및 로깅
            emitterRepository.deleteByUsername(username);
            log.error("SSE connection error", e);
        }


        return emitter;
    }
    
    // 로그인한 사용자의 읽지 않은 알림 리스트
    @GetMapping
    public List<Notification> getNotifications(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return notificationService.getUnreadNotificationsByUsername(userDetails.getUsername());
    }

}
