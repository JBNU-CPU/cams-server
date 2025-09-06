package com.cpu.cams.attendence.controller;

import com.cpu.cams.attendence.dto.response.CreateActivityAttendanceResponse;
import com.cpu.cams.attendence.dto.response.ParticipantActivityAttendanceResponse;
import com.cpu.cams.attendence.dto.response.SessionAttendanceResponse;
import com.cpu.cams.attendence.service.AttendanceService;
import com.cpu.cams.member.dto.response.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {


    private final AttendanceService attendanceService;

    // 출석하기 (출석 코드 인증)
    @PostMapping("/{sessionId}")
    public ResponseEntity<Long> attendance(
            @PathVariable Long sessionId,
            @RequestParam String attendancesCode,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        Long attendanceId = attendanceService.attendance(sessionId, attendancesCode, userDetails.getUsername());

        return ResponseEntity.ok().body(attendanceId);
    }

    // 지각/결석/출석 여부 업데이트
    @PutMapping("/{sessionId}/{participantId}")
    public ResponseEntity<Long> updateAttendancesStatus(
            @PathVariable Long sessionId,
            @PathVariable Long participantId,
            @RequestParam String attendanceStatus,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        Long attendanceId = attendanceService.updateAttendancesStatus(sessionId, participantId, attendanceStatus, customUserDetails);

        return ResponseEntity.ok().body(attendanceId);
    }
    
    // 내 출결 조회하기
    @GetMapping("/me")
    public ResponseEntity<Page<ParticipantActivityAttendanceResponse>> getMyAttendances(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ParticipantActivityAttendanceResponse> result = attendanceService.getMyAttendances(userDetails.getUsername(),page, size);

        return ResponseEntity.ok().body(result);
    }

    // 내가 개설한 활동 전체 출결 데이터 조회하기
    @GetMapping("/me/create")
    public ResponseEntity<List<CreateActivityAttendanceResponse>> getAllAttendances(@AuthenticationPrincipal UserDetails userDetails) {

        List<CreateActivityAttendanceResponse> myCreateActivityAttendances = attendanceService.getMyCreateActivityAttendances(userDetails.getUsername());

        return ResponseEntity.ok(myCreateActivityAttendances);
    }

    // 내가 개설한 특정 활동 전체 출결 데이터 조회하기
    @GetMapping("/me/create/{activityId}")
    public ResponseEntity<CreateActivityAttendanceResponse> getAttendance(@PathVariable Long activityId, @AuthenticationPrincipal UserDetails userDetails) {

        CreateActivityAttendanceResponse myCreateActivityAttendances = attendanceService.getAttendance(activityId, userDetails.getUsername());

        return ResponseEntity.ok(myCreateActivityAttendances);
    }
    
    // 특정 세션 전체 출결 데이터 리스트 조회하기
    @GetMapping("/{sessionId}")
    public ResponseEntity<List<SessionAttendanceResponse>> getSessionAttendances(
            @PathVariable Long sessionId,
            @AuthenticationPrincipal UserDetails userDetails) {
        List<SessionAttendanceResponse> sessionAttendances = attendanceService.getSessionAttendances(sessionId, userDetails.getUsername());
        return ResponseEntity.ok(sessionAttendances);
    }
    
    // 특정 멤버의 특정 활동에 대한 전체 출결 데이터 리스트 조회하기
    @GetMapping("/activity/{activityId}/member/{memberId}")
    public ResponseEntity<List<ParticipantActivityAttendanceResponse>> getMemberActivityAttendances(
            @PathVariable Long activityId,
            @PathVariable Long memberId,
            @AuthenticationPrincipal UserDetails userDetails) {
        List<ParticipantActivityAttendanceResponse> attendances = attendanceService.getMemberActivityAttendances(activityId, memberId, userDetails.getUsername());
        return ResponseEntity.ok(attendances);
    }
    
}
