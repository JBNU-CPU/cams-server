package com.cpu.cams.activity.controller;

import com.cpu.cams.activity.dto.response.ParticipantResponse;
import com.cpu.cams.activity.service.ActivityService;
import com.cpu.cams.activity.service.ParticipantService;
import com.cpu.cams.member.dto.response.CustomUserDetails;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activities/{activityId}/participant")
public class ParticipantController {

    private final ParticipantService participantsService;
    private final ActivityService activityService;

    // 전체 신청자 목록 조회
    @GetMapping
    public ResponseEntity<Page<ParticipantResponse>> getParticipants(
            @PathVariable Long activityId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Page<ParticipantResponse> result = participantsService.getActivityParticipants(activityId, customUserDetails, page, size);

        return ResponseEntity.ok(result);
    }

    // 신청 취소
    @DeleteMapping
    public String deleteParticipant(@PathVariable Long activityId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        participantsService.deleteParticipant(activityId, customUserDetails);
        return "OK";
    }

    // 신청 삭제
    @DeleteMapping("{participantId}")
    public void deleParticipantByReader(@PathVariable Long activityId, @PathVariable Long participantId, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        participantsService.deleteParticipantByReader(activityId, participantId, customUserDetails);
    }

    // 참가 신청
    @PostMapping
    public String addParticipant(@PathVariable Long activityId, @AuthenticationPrincipal UserDetails userDetails) {

        participantsService.addParticipant(activityId, userDetails.getUsername());

        return "OK";
    }

}
