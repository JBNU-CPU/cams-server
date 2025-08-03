package com.cpu.cams.activity.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    // 개설하기
    @PostMapping
    public String createActivity() {

        // todo: 포인트 지급 로직

        return "OK";
    }

    @PutMapping("/{activityId}")
    public String updateActivity(@PathVariable Long activityId) {
        return "OK";
    }

    @PutMapping("/{activityId}/closing-status")
    public String updateActivityClosingStatus(@PathVariable Long activityId) {
        return "OK";
    }

    @PostMapping("/search")
    public String searchActivity(@RequestParam String keyword) {
        return "OK";
    }
}
