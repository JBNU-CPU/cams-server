package com.cpu.cams.member.controller;

import com.cpu.cams.member.dto.request.ProfileRequest;
import com.cpu.cams.member.dto.request.ResetPasswordRequest;
import com.cpu.cams.member.dto.request.SignupRequest;
import com.cpu.cams.member.dto.request.WithdrawalRequest;
import com.cpu.cams.member.dto.response.ProfileResponse;
import com.cpu.cams.member.entity.Member;
import com.cpu.cams.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping
    public ResponseEntity<Long> signup(@RequestBody SignupRequest signupRequest) {
        Long memberId = memberService.signup(signupRequest);
        return ResponseEntity.ok().body(memberId);
    }

    // 비밀번호 찾기
    @PostMapping("/password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        // todo : 이메일 인증으로 구현
        return ResponseEntity.status(200).body("성공");
    }

    // 회원 탈퇴
    @DeleteMapping("/{memberId}")
    public ResponseEntity<?> deleteMemberInfo(@RequestBody WithdrawalRequest withdrawalRequest) {
        // todo : 이메일 인증으로 구현
        return ResponseEntity.status(200).body("성공");
    }


    // 내 프로필 조회
    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        ProfileResponse myProfile = memberService.getMyProfile(userDetails.getUsername());
        return ResponseEntity.ok(myProfile);
    }

    // 내 프로필 편집
    @PutMapping("/me")
    public ResponseEntity<Long> updateMyProfile(@RequestBody ProfileRequest profileRequest, @AuthenticationPrincipal UserDetails userDetails) {

        Long result = memberService.updateMyProfile(profileRequest, userDetails.getUsername());
        return ResponseEntity.ok(result);
    }

    // 나인지 판단하는 로직
    @GetMapping("/check")
    public ResponseEntity<Map<String, String>> checkMe(@AuthenticationPrincipal UserDetails userDetails){
        Member member = memberService.findByUsername(userDetails.getUsername());
        Map<String, String> result = new HashMap<>();
        result.put("name", member.getName());
        return ResponseEntity.ok(result);
    }
}
