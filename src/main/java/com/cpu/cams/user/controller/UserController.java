package com.cpu.cams.user.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    // 비밀번호 찾기
    @PostMapping("/password")
    public String findUserInfo(@RequestParam String email, @RequestParam String username) {
        return "usersId";
    }

    // 회원 탈퇴
    @DeleteMapping("/{userId}")
    public String deleteUserInfo(@PathVariable("userId") String userId) {
        return userId;
    }
    
    // 내가 개설한 활동 조회
    @GetMapping("/me/build-activity")
    public String getMyBuildActivities() {
        return "myActivity";
    }
    
    // 내가 참여한 활동 조회
    @GetMapping("/me/participate-activity")
    public String getMyParticipateActivities() {
        return "myParticipateActivity";
    }

    // 내 프로필 조회
    @GetMapping("/me")
    public String getMyProfile() {
        return "myProfile";
    }
}
