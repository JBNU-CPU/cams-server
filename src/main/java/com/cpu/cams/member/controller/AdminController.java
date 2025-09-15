package com.cpu.cams.member.controller;

import com.cpu.cams.activity.service.ActivityService;
import com.cpu.cams.member.dto.response.AdminActivityResponse;
import com.cpu.cams.member.dto.response.AdminMemberResponse;
import com.cpu.cams.member.entity.Role;
import com.cpu.cams.member.service.AdminService;
import com.cpu.cams.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    //임시
    private final AdminService adminService;
    private final MemberService memberService;
    private final ActivityService activityService;

    public AdminController(AdminService adminService, MemberService memberService, ActivityService activityService) {
        this.adminService = adminService;
        this.memberService = memberService;
        this.activityService = activityService;
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

    // 멤버 전체 조회
    @GetMapping("/members")
    public List<AdminMemberResponse> findAllMember(){
        List<AdminMemberResponse> all = memberService.findAll();
        return all;
    }

    // 활동 전체 조회
    @GetMapping("/activities")
    public List<AdminActivityResponse> findAllActivity(){
        List<AdminActivityResponse> all = activityService.findAll();
        return all;
    }


}
