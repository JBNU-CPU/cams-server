package com.cpu.cams.member.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Iterator;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping
    // 테스트
    public String index() {

        // 현재 사용자 role 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();
        System.out.println("role = " + role);
        return "<h1>Admin Page</h1>";
    }

    // 활동 승인 / 미승인
    @PostMapping("/activities/{activityId}")
    public String updateActivityApprovalStatus(@PathVariable Long activityId) {
        return "OK";
    }

    // 유저 승인 / 미승인
    @PostMapping("/members/{memberId}")
    public String updateMemberApprovalStatus(@PathVariable Long memberId) {
        return "OK";
    }
}
