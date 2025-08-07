package com.cpu.cams.activity.service;

import com.cpu.cams.activity.dto.request.ActivityRequest;
import com.cpu.cams.activity.dto.response.ActivityResponse;
import com.cpu.cams.activity.entity.Activity;
import com.cpu.cams.activity.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ActivityService {

    private final ActivityRepository activityRepository;

    // 개설하기
    public void createActivity(ActivityRequest activityRequest) {

        Activity activity = Activity.createActivity(activityRequest);
        activityRepository.save(activity);

        // todo: 포인트 지급 로직
    }


    public Page<ActivityResponse> getActivities(int page, int size) {
        Page<ActivityResponse> activities = activityRepository.findAll(PageRequest.of(page, size)).map(
                activity -> {
            return ActivityResponse.builder()
                    .id(activity.getId())
                    .title(activity.getTitle())
                    .description(activity.getDescription())
                    //.createdBy(activity.getCreatedBy().getName())
                    .recurringSchedules(ActivityResponse.convertRecurringSchedules(activity.getRecurringSchedules()))
                    .eventSchedules(ActivityResponse.convertEventSchedules(activity.getEventSchedules()))
                    .maxParticipants(activity.getMaxParticipants())
                    .participantCount(activity.getParticipantCount())
                    .activityType(activity.getActivityType().name())
                    .activityStatus(activity.getActivityStatus().name())
                    .build();
            }
        );

        return activities;
    }

    public ActivityResponse getActivity(Long activityId) {
        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("에러"));
        return ActivityResponse.builder()
                .id(activity.getId())
                .title(activity.getTitle())
                .description(activity.getDescription())
                .recurringSchedules(ActivityResponse.convertRecurringSchedules(activity.getRecurringSchedules()))
                .eventSchedules(ActivityResponse.convertEventSchedules(activity.getEventSchedules()))
                .maxParticipants(activity.getMaxParticipants())
                .participantCount(activity.getParticipantCount())
                .activityType(activity.getActivityType().name())
                .activityStatus(activity.getActivityStatus().name())
                .build();
    }

    public Long updateActivity(Long activityId, ActivityRequest activityRequest) {
        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("에러"));
        activity.updateActivity(activityRequest);

        return activity.getId();
    }

    public String updateStatus(Long activityId, String status) {
        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("에러"));
        activity.updateActivityStatus(status);
        return status;
    }

    public Long deleteActivity(Long activityId) {
        activityRepository.deleteById(activityId);
        return activityId;
    }
}
