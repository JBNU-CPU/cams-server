package com.cpu.cams.member.service;

import com.cpu.cams.activity.entity.Activity;
import com.cpu.cams.activity.repository.ActivityRepository;
import com.cpu.cams.member.entity.Member;
import com.cpu.cams.member.entity.Role;
import com.cpu.cams.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final ActivityRepository activityRepository;
    private final MemberRepository memberRepository;

    public boolean updateActivityApprovalStatus(Long activityId) {

        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("존재하지 않는 활동"));
        boolean approvalStatus = activity.updateActivityApprovalStatus();

        return approvalStatus;
    }

    public Role updateMemberRole(Long memberId, String role) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("존재하지 않는 유저"));
        return member.updateMemberRole(role);
    }
}
