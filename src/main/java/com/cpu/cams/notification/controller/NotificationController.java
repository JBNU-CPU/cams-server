package com.cpu.cams.notification.controller;

import com.cpu.cams.member.dto.response.CustomUserDetails;
import com.cpu.cams.notification.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@Slf4j
public class NotificationController {

    private final EmitterRepository emitterRepository;

    @GetMapping(value = "/stream")
    public SseEmitter streamNotifications(@AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return null; // 예외 처리 또는 적절한 응답
        }
        String username = userDetails.getUsername();
        SseEmitter emitter = new SseEmitter(60 * 1000L); // 1분

        // 기존 emitter가 있다면 삭제하고 새로 생성
        emitterRepository.deleteByUsername(username);
        emitterRepository.save(username, emitter); // 사용자별로 고유한 SseEmitter 객체 생성 -> 이 객체는 서버에서 클라이언트로 데이터를 보낼 수 있는 지속적인 단방향 연결 통로 역할 수행

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
}
