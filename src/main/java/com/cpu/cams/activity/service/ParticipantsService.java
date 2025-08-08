package com.cpu.cams.activity.service;

import com.cpu.cams.activity.entity.Activity;
import com.cpu.cams.activity.entity.ActivityParticipant;
import com.cpu.cams.activity.repository.ActivityParticipantRepository;
import com.cpu.cams.activity.repository.ActivityRepository;
import com.cpu.cams.member.entity.Member;
import com.cpu.cams.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@Transactional
@RequiredArgsConstructor
public class ParticipantsService {

    private final ActivityParticipantRepository activityParticipantRepository;
    private final MemberRepository memberRepository;
    private final ActivityRepository activityRepository;

    public void addParticipant(Long activityId) {
        Long memberId = 1L;
        // todo: 레포지토리로 불러오는게 맞니? MemberService에 함수 만들고 서비스로 불러오는게 맞니?
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("멤버없음"));
        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("활동 없음"));
        ActivityParticipant.create(activity, member);


    }
}
