package com.cpu.cams.activity.service;

import com.cpu.cams.activity.dto.request.ActivityRequest;
import com.cpu.cams.activity.dto.response.ActivityResponse;
import com.cpu.cams.activity.dto.response.CurriculumDTO;
import com.cpu.cams.activity.dto.response.EventScheduleDTO;
import com.cpu.cams.activity.dto.response.RecurringScheduleDTO;
import com.cpu.cams.activity.entity.Activity;
import com.cpu.cams.activity.entity.Curriculum;
import com.cpu.cams.activity.entity.EventSchedule;
import com.cpu.cams.activity.entity.RecurringSchedule;
import com.cpu.cams.activity.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ActivityService {

    private final ActivityRepository activityRepository;

    // 개설하기
    public void createActivity(ActivityRequest activityRequest) {

        Activity activity = Activity.createActivity(activityRequest);

        List<RecurringScheduleDTO> recurringSchedules = activityRequest.getRecurringSchedules();

        for (RecurringScheduleDTO recurringSchedule : recurringSchedules) {
            RecurringSchedule schedule = RecurringSchedule.create(recurringSchedule, activity);
        }

        List<EventScheduleDTO> eventSchedules = activityRequest.getEventSchedule();

        for (EventScheduleDTO eventSchedule : eventSchedules) {
            EventSchedule schedule = EventSchedule.create(eventSchedule, activity);
        }

        List<CurriculumDTO> curriculums = activityRequest.getCurriculums();

        for (CurriculumDTO curriculum : curriculums) {
            Curriculum curri = Curriculum.create(curriculum, activity);
        }



        activityRepository.save(activity);

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
                    .curriculums(ActivityResponse.convertCurriculums(activity.getCurriculums()))
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
