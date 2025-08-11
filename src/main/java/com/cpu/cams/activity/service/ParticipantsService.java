package com.cpu.cams.activity.service;

import com.cpu.cams.activity.dto.response.ParticipantsResponse;
import com.cpu.cams.activity.entity.Activity;
import com.cpu.cams.activity.entity.ActivityParticipant;
import com.cpu.cams.activity.repository.ActivityParticipantRepository;
import com.cpu.cams.activity.repository.ActivityRepository;
import com.cpu.cams.member.entity.Member;
import com.cpu.cams.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ParticipantsService {

    private final ActivityParticipantRepository activityParticipantRepository;
    private final MemberRepository memberRepository;
    private final ActivityRepository activityRepository;

    // 활동 참가 신청하기
    public void addParticipant(Long activityId) {
        Long memberId = 1L;
        // todo: 레포지토리로 불러오는게 맞니? MemberService에 함수 만들고 서비스로 불러오는게 맞니?
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("멤버없음"));
        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("활동 없음"));

        // 중복 참가 방지
        if(activityParticipantRepository.existsByMember(member)){
            throw new RuntimeException("당신 중복 참가자임");
        }

        ActivityParticipant.create(activity, member);

    }

    // 활동 참가자 목록 조회하기
    public Page<ParticipantsResponse> getParticipantActivities(Long activityId){
        Long memberId = 1l; // todo: 현재 사용자 즉 api 보낸 사람이 이 활동을 만든 사람이 맞는지 확인 작업 추가
        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("활동 없음"));

        PageRequest pageRequest = PageRequest.of(0, 3);
        Page<ActivityParticipant> participants = activityParticipantRepository.findByActivity(activity, pageRequest);

        Page<ParticipantsResponse> result = participants.map(participant ->
                ParticipantsResponse.builder()
                        .name(participant.getMember().getName())
                        .email(participant.getMember().getEmail())
                        .phone(participant.getMember().getPhone())
                        .joindAt(participant.getJoinedAt())
                        .build()
        );

        return result;
    }


}
