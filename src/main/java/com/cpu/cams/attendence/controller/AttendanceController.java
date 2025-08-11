package com.cpu.cams.attendence.controller;

import com.cpu.cams.activity.entity.ActivityStatus;
import com.cpu.cams.attendence.dto.response.CreateActivityAttendanceResponse;
import com.cpu.cams.attendence.dto.response.ParticipantsActivityAttendanceResponse;
import com.cpu.cams.attendence.entity.AttendanceStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {


    // 출석하기 (출석 코드 인증)
    @PostMapping("/{sessionId}/{activityParticipantsId}")
    public String attendance(@PathVariable String sessionId, @PathVariable String activityParticipantsId, @RequestBody String attendancesCode) {
        //todo: 포인트 지급 로직
        return "OK";
    }

    // 지각/결석/출석 여부 업데이트
    @PutMapping("/{sessionId}/{participantsId}")
    public String updateAttendancesStatus(@PathVariable String sessionId, @PathVariable String participantsId, @RequestBody AttendanceStatus attendanceStatus) {
        // request body에 지각/ 출석/ 결석 정보
        return "OK";
    }
    
    // 내 출결 조회하기
    @GetMapping("/me")
    public List<ParticipantsActivityAttendanceResponse> getMyAttendances() {
        return List.of(new ParticipantsActivityAttendanceResponse());
    }
    
    // 리더 : 내가 개설한 활동 전체 출결 데이터 조회하기
    @GetMapping("/me/create")
    public List<CreateActivityAttendanceResponse> getAllAttendances() {
        return List.of(new CreateActivityAttendanceResponse());
    }
}
