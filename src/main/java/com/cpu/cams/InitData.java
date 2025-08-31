package com.cpu.cams;

import com.cpu.cams.activity.dto.request.ActivityRequest;
import com.cpu.cams.activity.entity.ActivityType;
import com.cpu.cams.activity.service.ActivityService;
import com.cpu.cams.activity.service.ParticipantService;
import com.cpu.cams.attendence.dto.request.SessionRequest;
import com.cpu.cams.attendence.service.SessionService;
import com.cpu.cams.member.dto.request.SignupRequest;
import com.cpu.cams.member.service.MemberService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import com.cpu.cams.activity.dto.response.CurriculumDTO;
import com.cpu.cams.activity.dto.response.EventScheduleDTO;
import com.cpu.cams.activity.dto.response.RecurringScheduleDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class InitData {

    private final MemberService memberService;
    private final ActivityService activityService;
    private final ParticipantService participantService;
    private final SessionService sessionService;

    public InitData(MemberService memberService, ActivityService activityService, ParticipantService participantService, SessionService sessionService) {
        this.memberService = memberService;
        this.activityService = activityService;
        this.participantService = participantService;
        this.sessionService = sessionService;
    }

    @PostConstruct
    public void init(){
        memberService.signup(new SignupRequest("admin", "1234", "admin", "admin@gmail.com", "010-4444-4444", "컴공", 4));

        memberService.signup(new SignupRequest("test1", "1234", "test1", "test1@gmail.com", "010-1111-1111", "컴공", 1));
        memberService.signup(new SignupRequest("test2", "1234", "test2", "test2@gmail.com", "010-2222-2222", "컴공", 2));
        memberService.signup(new SignupRequest("test3", "1234", "test3", "test3@gmail.com", "010-3333-3333", "컴공", 3));

        memberService.signup(new SignupRequest("owner1", "1234", "owner1", "owner1@gmail.com", "010-5555-5555", "컴공", 1));
        memberService.signup(new SignupRequest("owner2", "1234", "owner2", "owner2@gmail.com", "010-6666-6666", "컴공", 2));
        memberService.signup(new SignupRequest("owner3", "1234", "owner3", "owner3@gmail.com", "010-7777-7777", "컴공", 3));

        // Create Activity
        List<RecurringScheduleDTO> recurringSchedules1 = Arrays.asList(
                new RecurringScheduleDTO(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0)),
                new RecurringScheduleDTO(DayOfWeek.WEDNESDAY, LocalTime.of(14, 0), LocalTime.of(16, 0))
        );
        List<CurriculumDTO> curriculums1 = Arrays.asList(
                new CurriculumDTO(1, "세션 1", "세션 첫 번째 커리큘럼"),
                new CurriculumDTO(2, "세션 2", "세션 두 번째 커리큘럼")
        );
        ActivityRequest activityRequest = new ActivityRequest(
            "임시 활동1", // title
            "owner1이 개설한 임시 활동입니다.", // description
            "테스트 목표", // goal
            ActivityType.SESSION, // activityType
            10, // maxParticipants
            "온라인", // location
            "참고 사항 없음", // notes
            recurringSchedules1, // recurringSchedules
            Collections.emptyList(), // eventSchedule
            curriculums1  // curriculums
        );

        Long newActivityId = activityService.createActivity(activityRequest, "owner1");

        // Add Participants
        participantService.addParticipant(newActivityId, "test1");
        participantService.addParticipant(newActivityId, "test2");

        // 세션 생성 요청 DTO
        SessionRequest sessionRequest = new SessionRequest(
                "0000",
                3
        );

        // 세션 생성
        sessionService.createSession(newActivityId, sessionRequest, "owner1");

        // Create Activity
        List<EventScheduleDTO> eventSchedules2 = Arrays.asList(
                new EventScheduleDTO(LocalDateTime.of(2025, 9, 1, 10, 0), LocalDateTime.of(2025, 9, 1, 12, 0)),
                new EventScheduleDTO(LocalDateTime.of(2025, 9, 5, 14, 0), LocalDateTime.of(2025, 9, 5, 16, 0))
        );
        List<CurriculumDTO> curriculums2 = Arrays.asList(
                new CurriculumDTO(1, "미팅 1", "미팅 첫 번째 커리큘럼"),
                new CurriculumDTO(2, "미팅 2", "미팅 두 번째 커리큘럼")
        );
        ActivityRequest activityRequest2 = new ActivityRequest(
                "임시 활동2", // title
                "owner2이 개설한 임시 활동입니다.", // description
                "테스트 목표", // goal
                ActivityType.MEETING, // activityType
                10, // maxParticipants
                "온라인", // location
                "참고 사항 없음", // notes
                Collections.emptyList(), // recurringSchedules
                eventSchedules2, // eventSchedule
                curriculums2  // curriculums
        );

        Long newActivityId2 = activityService.createActivity(activityRequest2, "owner2");

        // Add Participants
        participantService.addParticipant(newActivityId2, "test1");
        participantService.addParticipant(newActivityId2, "test2");

        // 세션 생성 요청 DTO
        SessionRequest sessionRequest2 = new SessionRequest(
                "0000",
                3
        );

        // 세션 생성
        sessionService.createSession(newActivityId2, sessionRequest2, "owner2");

        // Create Activity
        List<RecurringScheduleDTO> recurringSchedules3 = Arrays.asList(
                new RecurringScheduleDTO(DayOfWeek.TUESDAY, LocalTime.of(9, 0), LocalTime.of(11, 0)),
                new RecurringScheduleDTO(DayOfWeek.THURSDAY, LocalTime.of(13, 0), LocalTime.of(15, 0))
        );
        List<CurriculumDTO> curriculums3 = Arrays.asList(
                new CurriculumDTO(1, "스터디 1", "스터디 첫 번째 커리큘럼"),
                new CurriculumDTO(2, "스터디 2", "스터디 두 번째 커리큘럼")
        );
        ActivityRequest activityRequest3 = new ActivityRequest(
                "임시 활동3", // title
                "owner1이 개설한 임시 활동입니다.", // description
                "테스트 목표", // goal
                ActivityType.STUDY, // activityType
                10, // maxParticipants
                "온라인", // location
                "참고 사항 없음", // notes
                recurringSchedules3, // recurringSchedules
                Collections.emptyList(), // eventSchedule
                curriculums3  // curriculums
        );

        Long newActivityId3 = activityService.createActivity(activityRequest3, "owner1");

        // Add Participants
        participantService.addParticipant(newActivityId3, "test1");
        participantService.addParticipant(newActivityId3, "test2");

        // 세션 생성 요청 DTO
        SessionRequest sessionRequest3 = new SessionRequest(
                "0000",
                3
        );

        // 세션 생성
        sessionService.createSession(newActivityId3, sessionRequest3, "owner1");
    }
}
