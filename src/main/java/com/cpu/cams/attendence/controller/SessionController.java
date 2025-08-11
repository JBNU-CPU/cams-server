package com.cpu.cams.attendence.controller;

import com.cpu.cams.attendence.dto.request.SessionRequest;
import com.cpu.cams.attendence.dto.response.OpenSessionResponse;
import com.cpu.cams.attendence.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/session")
public class SessionController {

    private final SessionService sessionService;

    // 세션 생성
    @PostMapping("/{activityId}")
    public ResponseEntity<Long> createAttendance(@PathVariable Long activityId, @RequestBody SessionRequest sessionRequest) {

        Long sessionId = sessionService.createSession(activityId, sessionRequest);

        return ResponseEntity.ok(sessionId); // 출석 코드 응답
    }

    // 내가 신청한 활동 중 세션이 열린 활동 리스트 조회
    // todo: memberId 안건네줘도 되지않나?
    @GetMapping("/{memberId}/open-session")
    public ResponseEntity<Page<OpenSessionResponse>> getOpenSessionList(@PathVariable Long memberId) {

        Page<OpenSessionResponse> result = sessionService.findOpenSessionList(memberId);

        return ResponseEntity.ok(result);
    }

    // 출석 마감 여부 수정 -> 세션 상태 변경
    @PutMapping("/{sessionId}")
    public ResponseEntity<Long> toggleOpenAttendance(@PathVariable Long sessionId) {

        Long id = sessionService.toggleOpenAttendance(sessionId);
        return ResponseEntity.ok(id);
    }

    // 출석 코드 변경
    @PutMapping("/{sessionId}/attendance-code")
    public ResponseEntity<Long> updateAttendanceCode(@PathVariable Long sessionId, @RequestParam String attendanceCode) {
        Long id = sessionService.updateAttendanceCode(sessionId, attendanceCode);
        return ResponseEntity.ok(id);
    }

}
