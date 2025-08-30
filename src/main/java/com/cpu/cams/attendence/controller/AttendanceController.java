package com.cpu.cams.attendence.controller;

import com.cpu.cams.attendence.dto.response.CreateActivityAttendanceResponse;
import com.cpu.cams.attendence.dto.response.ParticipantActivityAttendanceResponse;
import com.cpu.cams.attendence.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {


    private final AttendanceService attendanceService;

    // 출석하기 (출석 코드 인증)
    @PostMapping("/{sessionId}")
    public ResponseEntity<Long> attendance(@PathVariable Long sessionId, @RequestParam String attendancesCode) {

//        // todo: 지우기
//        String username = userDetails != null
//                ? userDetails.getUsername()
//                : "init1";
//        log.info("username = {}", username);

        Long attendanceId = attendanceService.attendance(sessionId, attendancesCode);

        return ResponseEntity.ok().body(attendanceId);
    }

    // 지각/결석/출석 여부 업데이트
    @PutMapping("/{sessionId}/{participantId}")
    public ResponseEntity<Long> updateAttendancesStatus(@PathVariable Long sessionId, @PathVariable Long participantId, @RequestParam String attendanceStatus) {

        Long attendanceId = attendanceService.updateAttendancesStatus(sessionId, participantId, attendanceStatus);

        return ResponseEntity.ok().body(attendanceId);
    }
    
    // 내 출결 조회하기
    @GetMapping("/me")
    public ResponseEntity<Page<ParticipantActivityAttendanceResponse>> getMyAttendances() {
        Page<ParticipantActivityAttendanceResponse> result = attendanceService.getMyAttendances();

        return ResponseEntity.ok().body(result);
    }

    // todo:
    // 내가 개설한 활동 전체 출결 데이터 조회하기
    @GetMapping("/me/create")
    public List<CreateActivityAttendanceResponse> getAllAttendances() {

        // todo: 리더인지 확인
        List<CreateActivityAttendanceResponse> myCreateActivityAttendances = attendanceService.getMyCreateActivityAttendances();

        return myCreateActivityAttendances;
    }
}
