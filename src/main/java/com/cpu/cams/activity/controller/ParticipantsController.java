package com.cpu.cams.activity.controller;

import com.cpu.cams.activity.dto.response.ParticipantsResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities/{activityId}/participant")
public class ParticipantsController {

    // 전체 신청자 목록 조회
    @GetMapping
    public List<ParticipantsResponse> getParticipants(@PathVariable Long activityId) {
        return List.of(new ParticipantsResponse());
    }

    // 신청자 삭제
    @DeleteMapping("/{userId}")
    public String deleteParticipant(@PathVariable Long activityId, @PathVariable Long userId) {
        return "OK";
    }

    // 참가 신청
    @PostMapping()
    public String addParticipant(@PathVariable Long activityId) {
        return "OK";
    }
}
