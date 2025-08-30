package com.cpu.cams.test;

import com.cpu.cams.member.dto.response.CustomUserDetails;
import com.cpu.cams.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;

    /** (JWT 사용) 로그인 사용자 스트림 */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(
            @RequestHeader(value = "Last-Event-ID", required = false) String lastEventId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Long userId = resolveUserId(user);
        return notificationService.subscribe(userId, lastEventId);
    }

    /** (테스트) userId 지정 스트림 (permitAll로 열어두고 테스트 후 제거 권장) */
    @GetMapping(value = "/stream/test/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamTest(@PathVariable Long userId) {
        return notificationService.subscribe(userId, null);
    }

    /** 알림 생성 + 즉시 푸시 (예: 서버 내부/관리자/테스트) */
    @PostMapping("/send/{userId}")
    public NotificationResponse send(@PathVariable Long userId,
                                     @RequestBody NotificationPayload payload) {
        return notificationService.createAndSend(userId, payload);
    }

    /** 읽음 처리 */
    @PatchMapping("/{notificationId}/read")
    public void markRead(@PathVariable Long notificationId,
                         @AuthenticationPrincipal CustomUserDetails user) {
        Long userId = resolveUserId(user);
        notificationService.markRead(userId, notificationId);
    }

    /** (선택) 최근 알림 조회 */
    @GetMapping("/recent")
    public List<NotificationResponse> recent(@AuthenticationPrincipal CustomUserDetails user) {
        Long userId = resolveUserId(user);
        return notificationRepository.findTop50ByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(NotificationResponse::from)
                .toList();
    }

    // 전체 공지
    @PostMapping("/broadcast")
    public BroadcastResult broadcast(@RequestBody NotificationPayload payload) {
        return notificationService.broadcastToAll(payload);
    }

    // --- helper ---

    private Long resolveUserId(CustomUserDetails user) {
        try {
            // 네 구현체에 getUserId() 추가되면 이 경로 사용
            var method = user.getClass().getMethod("getUserId");
            Object id = method.invoke(user);
            if (id instanceof Long l) return l;
        } catch (Exception ignore) {}
        // 없다면 username -> id 조회
        return memberRepository.findIdByUsername(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));
    }
}
