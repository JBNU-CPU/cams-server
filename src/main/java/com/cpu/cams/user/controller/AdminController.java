package com.cpu.cams.user.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    // 활동 승인 / 미승인
    @PostMapping("/activities/{activityId}")
    public String updateActivityApprovalStatus(@PathVariable Long activityId) {
        return "OK";
    }

    // 유저 승인 / 미승인
    @PostMapping("/users/{userId}")
    public String updateUserApprovalStatus(@PathVariable Long userId) {
        return "OK";
    }
}
