package com.cpu.cams.member.service;

import com.cpu.cams.activity.entity.Activity;
import com.cpu.cams.activity.repository.ActivityRepository;
import com.cpu.cams.activity.service.ActivityService;
import com.cpu.cams.exception.CustomException;
import com.cpu.cams.member.entity.Member;
import com.cpu.cams.member.entity.Role;
import com.cpu.cams.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final ActivityRepository activityRepository;
    private final MemberRepository memberRepository;
    private final ActivityService activityService;

    public boolean updateActivityApprovalStatus(Long activityId) {

        Activity activity = activityService.findById(activityId);
        boolean approvalStatus = activity.updateActivityApprovalStatus();

        return approvalStatus;
    }

    public Role updateMemberRole(Long memberId, String role) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        return member.updateMemberRole(role);
    }
}
