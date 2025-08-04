package com.cpu.cams.activity.controller;

import com.cpu.cams.activity.dto.request.ActivityRequest;
import com.cpu.cams.activity.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    // 개설하기
    @PostMapping
    public String createActivity(@RequestBody ActivityRequest activityRequest) {
        // todo: 포인트 지급 로직
        activityService.createActivity(activityRequest);
        return "OK";
    }

    @PutMapping("/{activityId}")
    public String updateActivity(@PathVariable Long activityId, @RequestBody ActivityRequest activityRequest) {
        return "OK";
    }

    // 활동 상태 변경 (모집 중, 마감)
    @PutMapping("/{activityId}/closing-status")
    public String updateActivityClosingStatus(@PathVariable Long activityId) {
        return "OK";
    }

    // 검색
    @PostMapping("/search")
    public String searchActivity(@RequestParam String keyword) {
        return "OK";
    }
}
