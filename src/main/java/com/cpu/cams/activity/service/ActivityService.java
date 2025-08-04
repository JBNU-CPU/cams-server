package com.cpu.cams.activity.service;

import com.cpu.cams.activity.dto.request.ActivityRequest;
import com.cpu.cams.activity.entity.Activity;
import com.cpu.cams.activity.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    public void createActivity(ActivityRequest activityRequest) {
        Activity.ActivityRequestDto activityRequestDto = Activity.ActivityRequestDto.builder()
                .title(activityRequest.getTitle())
                .description(activityRequest.getDescription())
                .goal(activityRequest.getGoal())
                .activityType(activityRequest.getActivityType())
                .maxParticipants(activityRequest.getMaxParticipants())
                .location(activityRequest.getLocation())
                .notes(activityRequest.getNotes())
                .build();
        Activity activity = Activity.createActivity(activityRequestDto);
        activityRepository.save(activity);
    }

    // 개설하기

}
