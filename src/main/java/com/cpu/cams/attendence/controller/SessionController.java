package com.cpu.cams.attendence.controller;

import com.cpu.cams.attendence.dto.request.SessionRequest;
import com.cpu.cams.attendence.dto.response.OpenSessionResponse;
import com.cpu.cams.attendence.service.SessionService;
import jakarta.websocket.server.PathParam;
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
    public ResponseEntity<Long> createAttendance(@PathVariable Long activityId, @RequestBody SessionRequest sessionRequest, @AuthenticationPrincipal UserDetails userDetails) {

        Long sessionId = sessionService.createSession(activityId, sessionRequest, userDetails.getUsername());

        return ResponseEntity.ok(sessionId); // 출석 코드 응답
    }

    // 내가 신청한 활동 중 세션이 열린 활동 리스트 조회
    @GetMapping("/open-session")
    public ResponseEntity<Page<OpenSessionResponse>> getOpenSessionList(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size) {


        Page<OpenSessionResponse> result = sessionService.findOpenSessionList(userDetails.getUsername(), page, size);

        return ResponseEntity.ok(result);
    }

    // 출석 마감 여부 수정 -> 세션 상태 변경
    @PutMapping("/{sessionId}")
    public ResponseEntity<Long> toggleOpenAttendance(@PathVariable Long sessionId, @AuthenticationPrincipal UserDetails userDetails) {

        Long id = sessionService.closeSession(sessionId, userDetails.getUsername());
        return ResponseEntity.ok(id);
    }

    // 출석 코드 변경
    @PutMapping("/{sessionId}/attendance-code")
    public ResponseEntity<Long> updateAttendanceCode(@PathVariable Long sessionId, @RequestParam String attendanceCode, @AuthenticationPrincipal UserDetails userDetails) {

        Long id = sessionService.updateAttendanceCode(sessionId, attendanceCode, userDetails.getUsername());
        return ResponseEntity.ok(id);
    }

}
