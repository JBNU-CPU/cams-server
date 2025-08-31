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

import java.util.Collections;

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
        ActivityRequest activityRequest = new ActivityRequest(
            "임시 활동1", // title
            "owner1이 개설한 임시 활동입니다.", // description
            "테스트 목표", // goal
            ActivityType.GENERAL, // activityType
            10, // maxParticipants
            "온라인", // location
            "참고 사항 없음", // notes
            Collections.emptyList(), // recurringSchedules
            Collections.emptyList(), // eventSchedule
            Collections.emptyList()  // curriculums
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
        ActivityRequest activityRequest2 = new ActivityRequest(
                "임시 활동2", // title
                "owner2이 개설한 임시 활동입니다.", // description
                "테스트 목표", // goal
                ActivityType.GENERAL, // activityType
                10, // maxParticipants
                "온라인", // location
                "참고 사항 없음", // notes
                Collections.emptyList(), // recurringSchedules
                Collections.emptyList(), // eventSchedule
                Collections.emptyList()  // curriculums
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
    }
}
