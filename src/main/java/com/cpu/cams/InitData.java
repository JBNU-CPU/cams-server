package com.cpu.cams;

import com.cpu.cams.activity.dto.request.ActivityRequest;
import com.cpu.cams.activity.dto.response.RecurringScheduleDTO;
import com.cpu.cams.activity.entity.Activity;
import com.cpu.cams.activity.entity.ActivityType;
import com.cpu.cams.activity.service.ActivityService;
import com.cpu.cams.activity.service.ParticipantsService;
import com.cpu.cams.member.dto.request.SignupRequest;
import com.cpu.cams.member.service.MemberService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Controller;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

//@Component
@Controller
public class InitData {

    private final MemberService memberService;
    private final ActivityService activityService;
    private final ParticipantsService participantsService;

    public InitData(MemberService memberService, ActivityService activityService, ParticipantsService participantsService) {
        this.memberService = memberService;
        this.activityService = activityService;
        this.participantsService = participantsService;
    }

    @PostConstruct
    public void init(){

        Long member1Id = memberService.signup(new SignupRequest("init1", "1234", "test1", "test1@gmail.com", "010-1111-1111", "소공", 1));
        Long member2Id = memberService.signup(new SignupRequest("init2", "1234", "test2", "test2@gmail.com", "010-2222-2222", "소공", 2));
        Long member3Id = memberService.signup(new SignupRequest("init3", "1234", "test3", "test3@gmail.com", "010-3333-3333", "소공", 3));
        Long member4Id = memberService.signup(new SignupRequest("init4", "1234", "test4", "test4@gmail.com", "010-4444-4444", "소공", 4));

        Activity activity1 = activityService.createActivity(new ActivityRequest("테스트활동1", "테스트활동내용1", "화이팅1", ActivityType.SESSION, 10, "도서관1", "주의하시오1", List.of( // recurringSchedules
                new RecurringScheduleDTO(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0)),
                new RecurringScheduleDTO(DayOfWeek.WEDNESDAY, LocalTime.of(14, 0), LocalTime.of(16, 0)))
                , Collections.emptyList(), Collections.emptyList()));

        Activity activity2 = activityService.createActivity(new ActivityRequest("테스트활동2", "테스트활동내용2", "화이팅2", ActivityType.GENERAL, 11, "도서관2", "주의하시오2", List.of( // recurringSchedules
                new RecurringScheduleDTO(DayOfWeek.MONDAY, LocalTime.of(11, 30), LocalTime.of(13, 30)),
                new RecurringScheduleDTO(DayOfWeek.WEDNESDAY, LocalTime.of(15, 30), LocalTime.of(17, 30)))
                , Collections.emptyList(), Collections.emptyList()));

        Activity activity3 = activityService.createActivity(new ActivityRequest("테스트활동1", "테스트활동내용1", "화이팅1", ActivityType.PROJECT, 12, "도서관3", "주의하시오3", List.of( // recurringSchedules
                new RecurringScheduleDTO(DayOfWeek.MONDAY, LocalTime.of(12, 40), LocalTime.of(14, 40)),
                new RecurringScheduleDTO(DayOfWeek.WEDNESDAY, LocalTime.of(16, 40), LocalTime.of(18, 40)))
                , Collections.emptyList(), Collections.emptyList()));

        activityService.updateStatus(1L, "STARTED");

        // test1 테스트활동1, 테스트활동2 참가
        participantsService.addParticipant(activity1.getId(), member1Id);
        participantsService.addParticipant(activity2.getId(), member1Id);

        // test2 테스트활동1 참가
        participantsService.addParticipant(activity1.getId(), member2Id);

    }
}
