package com.cpu.cams.member.controller;

import com.cpu.cams.member.entity.Role;
import com.cpu.cams.member.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Iterator;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

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
    public ResponseEntity<Boolean> updateActivityApprovalStatus(@PathVariable Long activityId) {

        boolean approvalStatus = adminService.updateActivityApprovalStatus(activityId);
        return ResponseEntity.ok().body(approvalStatus);
    }

    // 유저 승인 / 미승인
    @PostMapping("/members/{memberId}")
    public ResponseEntity<Role> updateMemberApprovalStatus(@PathVariable Long memberId, @RequestParam String role) {

        Role updateMemberRole = adminService.updateMemberRole(memberId, role);
        return ResponseEntity.ok().body(updateMemberRole);
    }
}
