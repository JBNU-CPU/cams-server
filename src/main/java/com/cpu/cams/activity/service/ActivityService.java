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
import com.cpu.cams.exception.CustomException;
import com.cpu.cams.member.dto.response.CustomUserDetails;
import com.cpu.cams.member.entity.Member;
import com.cpu.cams.member.repository.MemberRepository;
import com.cpu.cams.point.PointConst;
import com.cpu.cams.point.dto.request.PointRequest;
import com.cpu.cams.point.entity.Point;
import com.cpu.cams.point.entity.PointType;
import com.cpu.cams.point.repository.PointRepository;
import com.cpu.cams.notification.NotificationPayload;
import com.cpu.cams.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;
    private final NotificationService notificationService;

    // 개설하기
    public Long createActivity(ActivityRequest activityRequest, String username) {

        Member findMember = memberRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("멤버없음"));
        Activity activity = Activity.create(activityRequest, findMember);

        List<RecurringScheduleDTO> recurringSchedules = activityRequest.getRecurringSchedules();

        //todo: Recurring, Event 둘 중 하나만 받도록 설정
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

        PointRequest pointRequest = PointRequest.builder()
                .type(PointType.CREATE.toString())
                .description(activity.getTitle() + " 개설")
                .amount(PointConst.CREATE_POINT)
                .build();

        Point point = Point.create(pointRequest, findMember);
        pointRepository.save(point);

        // 전체 공지
        NotificationPayload notificationPayload = new NotificationPayload("활동", activityRequest.getTitle() + " 개설!!", "~~~링크 참조");
        notificationService.broadcastToAll(notificationPayload);

        return activity.getId();
    }
    
    // 전체 활동 목록 조회
    public Page<ActivityResponse> getActivities(int page, int size) {
        Page<ActivityResponse> activities = activityRepository.findAll(PageRequest.of(page, size)).map(
                activity -> {
            return ActivityResponse.builder()
                    .creatorId(activity.getCreatedBy().getId())
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
                    .sessionCount(activity.getSessionCount())
                    .isApproved(activity.getIsApproved())
                    .build();
            }
        );

        return activities;
    }

    // 활동 세부 정보 조회
    public ActivityResponse getActivity(Long activityId) {
        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "활동을 찾을 수 없습니다."));

        return ActivityResponse.builder()
                .creatorId(activity.getCreatedBy().getId())
                .location(activity.getLocation())
                .id(activity.getId())
                .title(activity.getTitle())
                .goal(activity.getGoal())
                .notes(activity.getNotes())
                .description(activity.getDescription())
                .createdBy(activity.getCreatedBy().getName())
                .recurringSchedules(ActivityResponse.convertRecurringSchedules(activity.getRecurringSchedules()))
                .eventSchedules(ActivityResponse.convertEventSchedules(activity.getEventSchedules()))
                .curriculums(ActivityResponse.convertCurriculums(activity.getCurriculums()))
                .maxParticipants(activity.getMaxParticipants())
                .participantCount(activity.getParticipantCount())
                .activityType(activity.getActivityType().name())
                .activityStatus(activity.getActivityStatus().name())
                .sessionCount(activity.getSessionCount())
                .isApproved(activity.getIsApproved())
                .build();
    }

    // 활동 수정하기
    public Long updateActivity(Long activityId, ActivityRequest activityRequest, CustomUserDetails userDetails) {

        if (!isOwnerOrAdmin(userDetails, activityId)) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }
        
        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "활동을 찾을 수 없습니다."));

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

    // 활동 신청 마감
    public String updateStatus(Long activityId, String status, CustomUserDetails userDetails) {
        if (!isOwnerOrAdmin(userDetails, activityId)) {
            throw new AccessDeniedException("상태 변경 권한이 없습니다.");
        }
        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("에러"));
        activity.updateActivityStatus(status);
        return status;
    }

    public Long deleteActivity(Long activityId, CustomUserDetails userDetails) {
        if (!isOwnerOrAdmin(userDetails, activityId)) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }
        
        Member findMember = memberRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("멤버없음"));
        findMember.updateTotalPoints(-100);

        activityRepository.deleteById(activityId);
        return activityId;
    }

    public Page<ActivityResponse> searchActivity(String type, String keyword, int page, int size) {

        Page<Activity> activities;

        if ("titleContent".equals(type)) {
            activities = activityRepository.findByTitleContainingOrDescriptionContaining(keyword, keyword, PageRequest.of(page, size));
        } else if ("leader".equals(type)) {
            activities = activityRepository.findByCreatedBy_NameContaining(keyword, PageRequest.of(page, size));
        } else {
            // Or throw an exception for invalid type
            activities = Page.empty();
        }

        return activities.map(activity -> ActivityResponse.builder()
                .creatorId(activity.getCreatedBy().getId())
                .location(activity.getLocation())
                .id(activity.getId())
                .title(activity.getTitle())
                .goal(activity.getGoal())
                .notes(activity.getNotes())
                .description(activity.getDescription())
                .createdBy(activity.getCreatedBy().getName())
                .recurringSchedules(ActivityResponse.convertRecurringSchedules(activity.getRecurringSchedules()))
                .eventSchedules(ActivityResponse.convertEventSchedules(activity.getEventSchedules()))
                .curriculums(ActivityResponse.convertCurriculums(activity.getCurriculums()))
                .maxParticipants(activity.getMaxParticipants())
                .participantCount(activity.getParticipantCount())
                .activityType(activity.getActivityType().name())
                .activityStatus(activity.getActivityStatus().name())
                .sessionCount(activity.getSessionCount())
                .isApproved(activity.getIsApproved())
                .build());
    }
    
    // 활동 주인 및 관리자 확인 메서드
    private Boolean isOwnerOrAdmin(CustomUserDetails userDetails, Long activityId) {
        // 관리자 권한 확인
        if (userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        // 활동 소유자 확인
        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("활동이 없습니다."));
        return activity.getCreatedBy().getUsername().equals(userDetails.getUsername());
    }
}