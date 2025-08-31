package com.cpu.cams.activity.service;

import com.cpu.cams.activity.dto.response.ActivityResponse;
import com.cpu.cams.activity.repository.ActivityRepository;
import com.cpu.cams.member.entity.Member;
import com.cpu.cams.member.repository.MemberRepository;
import com.cpu.cams.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MyActivityService {
    private final ActivityRepository activityRepository;
    private final MemberService memberService;

    // 내가 개설한 활동 조회
    public Page<ActivityResponse> getMyCreateActivities(int page, int size, String username) {

        Member member = memberService.findByUsername(username);

        Page<ActivityResponse> myCreateActivities = activityRepository.findMyCreateActivityByCreatedBy(PageRequest.of(page, size), member).map(
                activity -> {
                    return ActivityResponse.builder()
                            .location(activity.getLocation())
                            .id(activity.getId())
                            .goal(activity.getGoal())
                            .notes(activity.getNotes())
                            .title(activity.getTitle())
                            .description(activity.getDescription())
                            .createdBy(activity.getCreatedBy().getName())
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

        return myCreateActivities;
    }

    // 내가 참여한 활동 조회
    public Page<ActivityResponse> getMyParticipateActivities(int page, int size, String username) {

        Member member = memberService.findByUsername(username);

        Page<ActivityResponse> myParticipateActivities = activityRepository.findMyParticipateActivitiesByMember(member, PageRequest.of(page, size)).map(
                activity -> {
                    return ActivityResponse.builder()
                            .location(activity.getLocation())
                            .id(activity.getId())
                            .goal(activity.getGoal())
                            .notes(activity.getNotes())
                            .title(activity.getTitle())
                            .description(activity.getDescription())
                            .createdBy(activity.getCreatedBy().getName())
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
        return myParticipateActivities;
    }
}
