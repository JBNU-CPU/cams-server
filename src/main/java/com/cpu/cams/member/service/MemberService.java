package com.cpu.cams.member.service;

import com.cpu.cams.exception.CustomException;
import com.cpu.cams.member.dto.request.ProfileRequest;
import com.cpu.cams.member.dto.request.ResetPasswordRequest;
import com.cpu.cams.member.dto.request.SignupRequest;
import com.cpu.cams.member.dto.response.AdminMemberResponse;
import com.cpu.cams.member.dto.response.ProfileResponse;
import com.cpu.cams.member.entity.EmailAuth;
import com.cpu.cams.member.entity.Member;
import com.cpu.cams.member.repository.EmailAuthRepository;
import com.cpu.cams.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.cpu.cams.member.dto.request.WithdrawalRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MailService mailService;
    private final EmailAuthRepository emailAuthRepository;

    public Long signup(SignupRequest signupRequest) {
        signupRequest.setPassword(bCryptPasswordEncoder.encode(signupRequest.getPassword()));
        Member member = Member.create(signupRequest);
        memberRepository.save(member);
        return member.getId();
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        Member member = memberRepository.findByUsername(resetPasswordRequest.getUsername())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        member.updatePassword(bCryptPasswordEncoder.encode(resetPasswordRequest.getPassword()));
    }

    public ProfileResponse getMyProfile(String username) {

        Member findMember = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        return ProfileResponse.builder()
                .introduce(findMember.getIntroduce())
                .interests(findMember.getInteresting())
                .id(findMember.getId())
                .email(findMember.getEmail())
                .name(findMember.getName())
                .phone(findMember.getPhone())
                .department(findMember.getDepartment())
                .cohort(findMember.getCohort())
                .totalPoints(findMember.getTotalPoints())
                .build();
    }

    public Long updateMyProfile(ProfileRequest profileRequest, String username) {
        Member findMember = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        Member update = findMember.update(profileRequest);
        return update.getId();
    }

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    public boolean checkAdmin(String username) {
        Member findMember = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        return findMember.getRole().equals(com.cpu.cams.member.entity.Role.ROLE_ADMIN);
    }

    // 인증 코드 발급
    @Transactional
    public boolean sendAuthcode(String email) throws MessagingException {
        String authCode = mailService.sendSimpleMessage(email);
        if(authCode != null){
            EmailAuth auth = emailAuthRepository.findByEmail(email);
            if(auth == null)
                emailAuthRepository.save(new EmailAuth(email, authCode));
            else
                auth.patch(authCode);
            return true;
        }
        return false;
    }

    // 이메일 / 인증 코드 검증
    public boolean validationAuthcode(String email, String authCode) {
        EmailAuth auth = emailAuthRepository.findByEmail(email);
        if(auth != null && auth.getAuthCode().equals(authCode)){
            emailAuthRepository.delete(auth);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public boolean checkEmailDuplication(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean checkUsernameDuplication(String username) {
        return memberRepository.existsByUsername(username);
    }

    public void withdrawal(WithdrawalRequest withdrawalRequest, UserDetails userDetails) {
        Member member = memberRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        if (!bCryptPasswordEncoder.matches(withdrawalRequest.getPassword(), member.getPassword())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        memberRepository.delete(member);
    }

    public List<AdminMemberResponse> findAll() {
        List<Member> all = memberRepository.findAll();
        List<AdminMemberResponse> result = all.stream()
                .map((member) -> AdminMemberResponse.entityToResponse(member)).toList();

        return result;
    }
}