package com.cpu.cams.activity.controller;

import com.cpu.cams.activity.dto.response.ActivityResponse;
import com.cpu.cams.activity.service.MyActivityService;
import com.cpu.cams.member.dto.response.MyParticipantsActivitiesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/me/activity")
@RequiredArgsConstructor
public class MyActivityController {

    private final MyActivityService myActivityService;

    // 내가 개설한 활동 조회
    @GetMapping("/build")
    public ResponseEntity<Page<ActivityResponse>> getMyBuildActivities(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<ActivityResponse> myBuildActivities = myActivityService.getMyBuildActivities(page, size);
        return ResponseEntity.ok().body(myBuildActivities);
    }

    // 내가 참여한 활동 조회
    @GetMapping("/participate")
    public List<MyParticipantsActivitiesResponse> getMyParticipateActivities() {
        return List.of(new MyParticipantsActivitiesResponse());
    }

}
