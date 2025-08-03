package com.cpu.cams.user.controller;

import com.cpu.cams.user.dto.request.ProfileRequest;
import com.cpu.cams.user.dto.request.ResetPasswordRequest;
import com.cpu.cams.user.dto.request.SignupRequest;
import com.cpu.cams.user.dto.request.WithdrawalRequest;
import com.cpu.cams.user.dto.response.MyBuildActivitiesResponse;
import com.cpu.cams.user.dto.response.MyParticipantsActivitiesResponse;
import com.cpu.cams.user.dto.response.ProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    // 회원가입
    @PostMapping
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        Long memberId = 1L;
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(memberId)
                .toUri();
        return ResponseEntity.created(location).body("회원 가입 성공");
    }

    // 비밀번호 찾기
    @PostMapping("/password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        return ResponseEntity.status(200).body("성공");
    }

    // 회원 탈퇴
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUserInfo(@RequestBody WithdrawalRequest withdrawalRequest) {
        return ResponseEntity.status(200).body("성공");
    }

    // 내가 개설한 활동 조회
    @GetMapping("/me/build-activity")
    public List<MyBuildActivitiesResponse> getMyBuildActivities() {
        return List.of(new MyBuildActivitiesResponse());
    }

    // 내가 참여한 활동 조회
    @GetMapping("/me/participate-activity")
    public List<MyParticipantsActivitiesResponse> getMyParticipateActivities() {
        return List.of(new MyParticipantsActivitiesResponse());
    }

    // 내 프로필 조회
    @GetMapping("/me")
    public ProfileResponse getMyProfile() {
        return new ProfileResponse();
    }

    // 내 프로필 편집
    @PutMapping("/me")
    public String updateMyProfile(@RequestBody ProfileRequest profileRequest) {
        return "ok";
    }
}
