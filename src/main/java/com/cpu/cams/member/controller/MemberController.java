package com.cpu.cams.member.controller;

import com.cpu.cams.member.dto.request.ProfileRequest;
import com.cpu.cams.member.dto.request.ResetPasswordRequest;
import com.cpu.cams.member.dto.request.SignupRequest;
import com.cpu.cams.member.dto.request.WithdrawalRequest;
import com.cpu.cams.member.dto.response.ProfileResponse;
import com.cpu.cams.member.entity.Member;
import com.cpu.cams.member.service.MailService;
import com.cpu.cams.member.service.MemberService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final MailService mailService;

    // 회원가입
    @PostMapping
    public ResponseEntity<Long> signup(@Valid @RequestBody SignupRequest signupRequest) {
        System.out.println("바뀌나요??");
        Long memberId = memberService.signup(signupRequest);
        return ResponseEntity.ok().body(memberId);
    }

    // 비밀번호 찾기
    @PostMapping("/password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        memberService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok().body("비밀번호가 재설정되었습니다.");
    }

    // 회원 탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteMemberInfo(@Valid @RequestBody WithdrawalRequest withdrawalRequest, @AuthenticationPrincipal UserDetails userDetails) {
        memberService.withdrawal(withdrawalRequest, userDetails);
        return ResponseEntity.ok().body("회원 탈퇴가 완료되었습니다.");
    }


    // 내 프로필 조회
    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        ProfileResponse myProfile = memberService.getMyProfile(userDetails.getUsername());
        return ResponseEntity.ok(myProfile);
    }

    // 내 프로필 편집
    @PutMapping("/me")
    public ResponseEntity<Long> updateMyProfile(@Valid @RequestBody ProfileRequest profileRequest, @AuthenticationPrincipal UserDetails userDetails) {

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

    // 이메일 인증 코드 전송 요청
    @GetMapping("/email/auth")
    public ResponseEntity<String> requestAuthcode(@RequestParam(name = "email") String email) throws MessagingException {
        boolean isSend = memberService.sendAuthcode(email);
        return isSend ? ResponseEntity.status(HttpStatus.OK).body("인증 코드가 전송되었습니다.") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 코드 전송이 실패하였습니다.");
    }
    
    // 이메일 인증
    @PostMapping("/email/auth")
    public ResponseEntity<String> validateAuthcode(@RequestParam(name = "email")String email,
                                                   @RequestParam(name = "auth")String authCode) {
        boolean isSuccess = memberService.validationAuthcode(email, authCode);
        return isSuccess ? ResponseEntity.status(HttpStatus.OK).body("이메일 인증에 성공하였습니다.") :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일 인증에 실패하였습니다.");
    }

    // 이메일 중복 확인
    @GetMapping("/check-email")
    public ResponseEntity<Void> checkEmail(@RequestParam String email) {
        if (memberService.checkEmailDuplication(email)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    // 학번 중복 확인
    @GetMapping("/check-username")
    public ResponseEntity<Void> checkUsername(@RequestParam String username) {
        if (memberService.checkUsernameDuplication(username)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}
