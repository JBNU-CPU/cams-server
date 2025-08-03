package com.cpu.cams.attendence.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {


    // 출석하기 (출석 코드 인증)
    @PostMapping("/{sessionId}/{activity_participants_id}/{attendances_code}")
    public String attendance(@PathVariable String activityId, @PathVariable String userId, @PathVariable String attendancesCode) {
        //todo: 포인트 지급 로직
        return "OK";
    }

    // 출석 마감 여부 수정 -> 세션 상태 변경
    @PutMapping("/{sessionId}")
    public String updateSessionStatus(@PathVariable String sessionId) {
        return "OK";
    }

    // 지각/결석/출석 여부 업데이트
    @PutMapping("/{sessionId}/{participantsId}")
    public String updateAttendancesStatus(@PathVariable String sessionId, @PathVariable String participantsId) {
        // request body에 지각/ 출석/ 결석 정보
        return "OK";
    }
    
    // 내 출결 조회하기
    @GetMapping("/me")
    public String getMyAttendances() {
        return "OK";
    }
    
    // 리더 : 내가 개설한 활동 전체 출결 데이터 조회하기
    @GetMapping("/leader/{activityId}")
    public String getAllAttendances(@PathVariable Long activityId) {
        return "OK";
    }
}
