package com.cpu.cams.activity.service;

import com.cpu.cams.activity.dto.response.ActivityResponse;
import com.cpu.cams.activity.entity.Activity;
import com.cpu.cams.activity.repository.ActivityRepository;
import com.cpu.cams.member.entity.Member;
import com.cpu.cams.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MyActivityService {
    private final ActivityRepository activityRepository;
    private final MemberRepository memberRepository;

    public MyActivityService(ActivityRepository activityRepository, MemberRepository memberRepository) {
        this.activityRepository = activityRepository;
        this.memberRepository = memberRepository;
    }

    public Page<ActivityResponse> getMyBuildActivities(int page, int size) {
        // todo: memberId security 에서 불러오기
        Long memberId = 1L;
        Member member = memberRepository.findById(memberId).orElse(null);

        Page<ActivityResponse> myBuildActivities = activityRepository.findMyBuildActivityByCreatedBy(PageRequest.of(page, size), member).map(
                activity -> {
                    return ActivityResponse.builder()
                            .id(activity.getId())
                            .title(activity.getTitle())
                            .description(activity.getDescription())
                            //.createdBy(activity.getCreatedBy().getName())
                            .recurringSchedules(ActivityResponse.convertRecurringSchedules(activity.getRecurringSchedules()))
                            .eventSchedules(ActivityResponse.convertEventSchedules(activity.getEventSchedules()))
                            .curriculums(ActivityResponse.convertCurriculums(activity.getCurriculums()))
                            .maxParticipants(activity.getMaxParticipants())
                            .participantCount(activity.getParticipantCount())
                            .activityType(activity.getActivityType().name())
                            .activityStatus(activity.getActivityStatus().name())
                            .build();
                }
        );


        return myBuildActivities;
    }
}
