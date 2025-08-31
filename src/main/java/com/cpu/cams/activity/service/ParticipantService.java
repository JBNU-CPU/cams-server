package com.cpu.cams.activity.service;

import com.cpu.cams.activity.dto.response.ParticipantResponse;
import com.cpu.cams.activity.entity.Activity;
import com.cpu.cams.activity.entity.ActivityParticipant;
import com.cpu.cams.activity.repository.ActivityParticipantRepository;
import com.cpu.cams.activity.repository.ActivityRepository;
import com.cpu.cams.member.dto.response.CustomUserDetails;
import com.cpu.cams.member.entity.Member;
import com.cpu.cams.member.repository.MemberRepository;
import com.cpu.cams.member.service.MemberService;
import com.cpu.cams.notification.NotificationPayload;
import com.cpu.cams.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ParticipantService {

    private final ActivityParticipantRepository activityParticipantRepository;
    private final MemberRepository memberRepository;
    private final ActivityRepository activityRepository;
    private final MemberService memberService;
    private final NotificationService notificationService;

    // 활동 참가 신청하기
    public void addParticipant(Long activityId, String username) {

        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("멤버없음"));
        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("활동 없음"));

        // 중복 참가 방지
        boolean duplicated = activity.getParticipants().stream()
                .anyMatch(participant -> participant.getMember().getId().equals(member.getId()));
        if(duplicated){
            throw new RuntimeException("당신 중복 참가자임");
        }

        // 팀장 참가 금지
        if(username.equals(activity.getCreatedBy().getUsername())){
            throw new RuntimeException("너가 만든걸 너가 신청할려고?");
        }

        ActivityParticipant.create(activity, member);

        NotificationPayload notificationPayload = new NotificationPayload("참가 신청", member.getUsername() + "님이 "+activity.getTitle() + " 활동을 참가하고 싶어합니다", "~~~링크 참조");

        // 팀장에게 알림
        notificationService.createAndSend(activity.getCreatedBy().getId(), notificationPayload);

    }

    // 활동 참가자 목록 조회하기
    public Page<ParticipantResponse> getActivityParticipants(Long activityId, CustomUserDetails customUserDetails, int page, int size){

        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("활동 없음"));

        if(!activity.getCreatedBy().getUsername().equals(customUserDetails.getUsername()) && !checkAdmin(customUserDetails)){
            throw new RuntimeException("당신 팀장 맞아?");
        }

        Page<ActivityParticipant> participants = activityParticipantRepository.findByActivity(activity, PageRequest.of(page, size));

        Page<ParticipantResponse> result = participants.map(participant ->
                ParticipantResponse.builder()
                        .id(participant.getId())
                        .name(participant.getMember().getName())
                        .email(participant.getMember().getEmail())
                        .phone(participant.getMember().getPhone())
                        .joindAt(participant.getJoinedAt())
                        .build()
        );

        return result;
    }

    @Transactional
    // 활동 신청자 취소
    public void deleteParticipant(Long activityId, CustomUserDetails customUserDetails){

        // 멤버가 실제 참가했었는지 확인
        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("활동 없음"));

        String username = customUserDetails.getUsername();
        activity.getParticipants().stream().filter((participant -> participant.getMember().getUsername().equals(username))).findFirst().orElseThrow(() -> new RuntimeException("당신 참가자 아닌거같음"));

        Member findMember = memberService.findByUsername(username);

        ActivityParticipant participant = activityParticipantRepository.findByMemberAndActivity(findMember, activity).orElseThrow(() -> new RuntimeException("참가자 아닌거같아요"));
        activity.getParticipants().remove(participant);

        activityParticipantRepository.delete(participant);
        // 참가자 줄이기
        activity.cancelParticipant();
        log.info("활동 신청자 취소 ={}", participant.getId());
    }

    // 활동 주인 및 관리자 확인 메서드
    private Boolean checkAdmin(CustomUserDetails customUserDetails) {
        // 관리자 권한 확인
        if (customUserDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }
        return false;
    }

    // 참가자 삭제하기
    public void deleteParticipantByReader(Long activityId, Long participantId, CustomUserDetails customUserDetails) {

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("활동 없음"));

        // 권한 체크 (팀장 or 관리자만 가능)
        if (!activity.getCreatedBy().getUsername().equals(customUserDetails.getUsername())
                && !checkAdmin(customUserDetails)) {
            throw new RuntimeException("당신 팀장 맞아?");
        }

        // 해당 멤버 참가자 찾아 삭제
        ActivityParticipant participant = activityParticipantRepository.findById(participantId).orElseThrow(() -> new RuntimeException("참가자 아님"));
        activityParticipantRepository.delete(participant);

        // 참가자 줄이기
        activity.cancelParticipant();
    }

}
