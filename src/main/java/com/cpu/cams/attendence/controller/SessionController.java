package com.cpu.cams.attendence.controller;

import com.cpu.cams.attendence.dto.request.SessionRequest;
import com.cpu.cams.attendence.dto.response.OpenSessionResponse;
import com.cpu.cams.attendence.dto.response.SessionInfoResponse;
import com.cpu.cams.attendence.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/session")
public class SessionController {

    private final SessionService sessionService;

    // 세션 생성
    @PostMapping("/{activityId}")
    public ResponseEntity<Long> createAttendance(@PathVariable Long activityId, @Valid @RequestBody SessionRequest sessionRequest, @AuthenticationPrincipal UserDetails userDetails) {

        Long sessionId = sessionService.createSession(activityId, sessionRequest, userDetails.getUsername());

        return ResponseEntity.ok(sessionId); // 출석 코드 응답
    }

    // 내가 신청한 활동 중 세션이 열린 활동 리스트 조회
    @GetMapping("/open-session")
    public ResponseEntity<Page<OpenSessionResponse>> getOpenSessionList(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {


        Page<OpenSessionResponse> result = sessionService.findOpenSessionList(userDetails.getUsername(), page, size);

        return ResponseEntity.ok(result);
    }

    // 출석 수동 마감
    @PutMapping("/{sessionId}")
    public ResponseEntity<Long> closeSession (@PathVariable Long sessionId, @AuthenticationPrincipal UserDetails userDetails) {

        Long id = sessionService.closeSession(sessionId, userDetails.getUsername());
        return ResponseEntity.ok(id);
    }

    // 세션 재오픈 (출석코드, 마감기한)
    @PatchMapping("/{sessionId}/info")
    public ResponseEntity<Long> updateSessionInfo
    (
            @PathVariable Long sessionId,
            @Valid @RequestBody SessionRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long id = sessionService.updateSessionInfo(sessionId, request, userDetails.getUsername());
        return ResponseEntity.ok(id);
    }

    // 해당 활동의 모든 세션 조회
    @GetMapping("/activity/{activityId}")
    public ResponseEntity<Page<SessionInfoResponse>> getSessionsByActivity
    (
            @PathVariable Long activityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<SessionInfoResponse> result = sessionService.findSessionsByActivity(activityId, page, size);
        return ResponseEntity.ok(result);
    }
    
    // 해당 활동의 진행 중인 세션 (OPEN OR CLOSED) 조회
    @GetMapping("/activity/{activityId}/ongoing")
    public ResponseEntity<SessionInfoResponse> getOngoingSessionsByActivity(@PathVariable Long activityId) {
        SessionInfoResponse result = sessionService.findOngoingSessionsByActivity(activityId);
        return ResponseEntity.ok(result);
    }
}
