package com.cpu.cams.activity.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activities/{activityId}/participant")
public class ParticipantsController {

    // 전체 신청자 목록 조회
    @GetMapping
    public String getParticipants(@PathVariable Long activityId) {
        return "OK";
    }

    // 신청자 삭제
    @DeleteMapping("/{userId}")
    public String deleteParticipant(@PathVariable Long activityId, @PathVariable Long userId) {
        return "OK";
    }

    // 참가 신청
    @PostMapping("/{userId}")
    public String addParticipant(@PathVariable Long activityId, @PathVariable Long userId) {
        return "OK";
    }
}
