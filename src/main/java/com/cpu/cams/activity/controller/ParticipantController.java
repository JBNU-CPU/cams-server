package com.cpu.cams.activity.controller;

import com.cpu.cams.activity.dto.response.ParticipantResponse;
import com.cpu.cams.activity.service.ActivityService;
import com.cpu.cams.activity.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activities/{activityId}/participant")
public class ParticipantController {

    private final ParticipantService participantsService;
    private final ActivityService activityService;

    // 전체 신청자 목록 조회
    @GetMapping
    public ResponseEntity<Page<ParticipantResponse>> getParticipants(@PathVariable Long activityId) {

        Page<ParticipantResponse> result = participantsService.getActivityParticipants(activityId);

        return ResponseEntity.ok(result);
    }

    // 신청자 삭제
    @DeleteMapping("/{participantId}")
    public String deleteParticipant(@PathVariable Long activityId, @PathVariable Long participantId) {
        participantsService.deleteParticipant(activityId, participantId);
        return "OK";
    }

    // 참가 신청
    @PostMapping
    public String addParticipant(@PathVariable Long activityId) {

        participantsService.addParticipant(activityId);

        return "OK";
    }
}
