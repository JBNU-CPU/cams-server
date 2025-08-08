package com.cpu.cams.activity.controller;

import com.cpu.cams.activity.dto.response.ParticipantsResponse;
import com.cpu.cams.activity.service.ActivityService;
import com.cpu.cams.activity.service.ParticipantsService;
import com.cpu.cams.member.repository.MemberRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities/{activityId}/participant")
public class ParticipantsController {

    private final ParticipantsService participantsService;
    private final ActivityService activityService;

    public ParticipantsController(ParticipantsService participantsService, ActivityService activityService) {
        this.participantsService = participantsService;
        this.activityService = activityService;
    }

    // 전체 신청자 목록 조회
    @GetMapping
    public List<ParticipantsResponse> getParticipants(@PathVariable Long activityId) {
        return List.of(new ParticipantsResponse());
    }

    // 신청자 삭제
    @DeleteMapping("/{memberId}")
    public String deleteParticipant(@PathVariable Long activityId, @PathVariable Long memberId) {
        return "OK";
    }

    // 참가 신청
    @PostMapping()
    public String addParticipant(@PathVariable Long activityId) {

        participantsService.addParticipant(activityId);

        return "OK";
    }
}
