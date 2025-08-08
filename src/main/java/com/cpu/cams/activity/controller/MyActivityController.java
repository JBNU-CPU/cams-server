package com.cpu.cams.activity.controller;

import com.cpu.cams.activity.dto.response.ActivityResponse;
import com.cpu.cams.activity.service.MyActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/me/activity")
@RequiredArgsConstructor
public class MyActivityController {

    private final MyActivityService myActivityService;

    // 내가 개설한 활동 조회
    @GetMapping("/create")
    public ResponseEntity<Page<ActivityResponse>> getMyCreateActivities(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<ActivityResponse> myCreateActivities = myActivityService.getMyCreateActivities(page, size);
        return ResponseEntity.ok().body(myCreateActivities);
    }

    // 내가 참여한 활동 조회
    @GetMapping("/participate")
    public ResponseEntity<Page<ActivityResponse>> getMyParticipateActivities(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<ActivityResponse> myParticipateActivities = myActivityService.getMyParticipateActivities(page, size);
        return ResponseEntity.ok().body(myParticipateActivities);
    }

}
