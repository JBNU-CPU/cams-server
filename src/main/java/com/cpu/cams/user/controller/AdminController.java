package com.cpu.cams.user.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @PostMapping("/activities/{activityId}")
    public String updateActivityApprovalStatus(@PathVariable Long activityId) {
        return "OK";
    }
}
