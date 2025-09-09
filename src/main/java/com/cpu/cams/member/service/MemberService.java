package com.cpu.cams.member.service;

import com.cpu.cams.exception.CustomException;
import com.cpu.cams.member.dto.request.ProfileRequest;
import com.cpu.cams.member.dto.request.SignupRequest;
import com.cpu.cams.member.dto.response.ProfileResponse;
import com.cpu.cams.member.entity.Member;
import com.cpu.cams.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long signup(SignupRequest signupRequest) {
        signupRequest.setPassword(bCryptPasswordEncoder.encode(signupRequest.getPassword()));
        Member member = Member.create(signupRequest);
        memberRepository.save(member);
        return member.getId();
    }

    public ProfileResponse getMyProfile(String username) {

        Member findMember = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        return ProfileResponse.builder()
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
}