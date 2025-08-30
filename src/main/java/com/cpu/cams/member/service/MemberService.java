package com.cpu.cams.member.service;

import com.cpu.cams.member.dto.request.ProfileRequest;
import com.cpu.cams.member.dto.request.SignupRequest;
import com.cpu.cams.member.dto.response.ProfileResponse;
import com.cpu.cams.member.entity.Member;
import com.cpu.cams.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
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

        Member findMember = memberRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("멤버없음"));
        return ProfileResponse.builder()
                .email(findMember.getEmail())
                .name(findMember.getName())
                .phone(findMember.getPhone())
                .department(findMember.getDepartment())
                .cohort(findMember.getCohort())
                .totalPoints(findMember.getTotalPoints())
                .build();
    }

    public Long updateMyProfile(ProfileRequest profileRequest, String username) {
        Member findMember = memberRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("멤버없음"));
        Member update = findMember.update(profileRequest);
        return update.getId();
    }
}
