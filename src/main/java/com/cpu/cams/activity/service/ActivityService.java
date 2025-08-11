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
import com.cpu.cams.member.entity.Member;
import com.cpu.cams.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final MemberRepository memberRepository;

    // 개설하기
    public void createActivity(ActivityRequest activityRequest) {

        //todo: 시큐리티에서 사용자 정보 가져오기
        Long memberId = 1l;
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("멤버없음"));
        Activity activity = Activity.create(activityRequest, findMember);

        List<RecurringScheduleDTO> recurringSchedules = activityRequest.getRecurringSchedules();

        for (RecurringScheduleDTO recurringSchedule : recurringSchedules) {
            RecurringSchedule.create(recurringSchedule, activity);
        }

        List<EventScheduleDTO> eventSchedules = activityRequest.getEventSchedule();

        for (EventScheduleDTO eventSchedule : eventSchedules) {
            EventSchedule.create(eventSchedule, activity);
        }

        List<CurriculumDTO> curriculums = activityRequest.getCurriculums();

        for (CurriculumDTO curriculum : curriculums) {
            Curriculum.create(curriculum, activity);
        }



        activityRepository.save(activity);

    }
    
    // 전체 활동 목록 조회
    public Page<ActivityResponse> getActivities(int page, int size) {
        Page<ActivityResponse> activities = activityRepository.findAll(PageRequest.of(page, size)).map(
                activity -> {
            return ActivityResponse.builder()
                    .id(activity.getId())
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

        return activities;
    }

    // 활동 세부 정보 조회
    public ActivityResponse getActivity(Long activityId) {
        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("운동 없음"));
        return ActivityResponse.builder()
                .id(activity.getId())
                .title(activity.getTitle())
                .description(activity.getDescription())
                .createdBy(activity.getCreatedBy().getName())
                .recurringSchedules(ActivityResponse.convertRecurringSchedules(activity.getRecurringSchedules()))
                .eventSchedules(ActivityResponse.convertEventSchedules(activity.getEventSchedules()))
                .maxParticipants(activity.getMaxParticipants())
                .participantCount(activity.getParticipantCount())
                .activityType(activity.getActivityType().name())
                .activityStatus(activity.getActivityStatus().name())
                .build();
    }

    public Long updateActivity(Long activityId, ActivityRequest activityRequest) {
        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("운동 없음"));

        activity.updateActivity(activityRequest);

        // 자식 엔티티들을 업데이트합니다.
        // orphanRemoval=true 옵션 덕분에, 컬렉션을 clear하고 새로 추가하면
        // JPA가 알아서 기존 자식들을 DELETE하고 새로운 자식들을 INSERT합니다.

        // --- 반복 일정 (Recurring Schedules) ---
        activity.getRecurringSchedules().clear();
        for (RecurringScheduleDTO recurringSchedule : activityRequest.getRecurringSchedules()) {
            RecurringSchedule.create(recurringSchedule, activity);
        }

        // --- 이벤트 일정 (Event Schedules) ---
        activity.getEventSchedules().clear();
        for (EventScheduleDTO eventSchedule : activityRequest.getEventSchedule()) {
            EventSchedule.create(eventSchedule, activity);
        }

        // --- 커리큘럼 (Curriculums) ---
        activity.getCurriculums().clear();
        for (CurriculumDTO curriculum : activityRequest.getCurriculums()) {
            Curriculum.create(curriculum, activity);
        }

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
