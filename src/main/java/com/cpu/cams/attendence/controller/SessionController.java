package com.cpu.cams.attendence.controller;

import com.cpu.cams.attendence.entity.Attendance;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    // 세션 생성
    @PostMapping("/{activityId}")
    public String createAttendance(@RequestBody Attendance attendance) {

        // TODO 세션에 따른 모든 참여 학생 attendance 객체 생성

        return "OK"; // 출석 코드 응답
    }

    // 내가 신청한 활동 중 세션이 열린 활동 리스트 조회
    // 1. userid -> activity_partipants에서 activity조회해서 그 중에 session이 start인걸 조회한다~ 리턴값으로는 activity, session 둘 다 줘야 한다.
    @GetMapping("/{userId}/open-session")
    public String getOpenSessionList(@PathVariable String userId) {
        return "OK";
    }

}
