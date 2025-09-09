package com.cpu.cams.attendence.service;

import com.cpu.cams.activity.entity.Activity;
import com.cpu.cams.activity.entity.ActivityParticipant;
import com.cpu.cams.activity.repository.ActivityParticipantRepository;
import com.cpu.cams.activity.repository.ActivityRepository;
import com.cpu.cams.activity.service.ActivityService;
import com.cpu.cams.attendence.dto.response.CreateActivityAttendanceResponse;
import com.cpu.cams.attendence.dto.response.SessionAttendanceResponse;
import com.cpu.cams.attendence.entity.SessionStatus;
import com.cpu.cams.attendence.repository.SessionRepository;
import com.cpu.cams.attendence.dto.response.ParticipantActivityAttendanceResponse;
import com.cpu.cams.attendence.entity.Attendance;
import com.cpu.cams.attendence.entity.AttendanceStatus;
import com.cpu.cams.attendence.entity.Session;
import com.cpu.cams.attendence.repository.AttendanceRepository;
import com.cpu.cams.exception.CustomException;
import com.cpu.cams.member.dto.response.CustomUserDetails;
import com.cpu.cams.member.entity.Member;
import com.cpu.cams.member.repository.MemberRepository;
import com.cpu.cams.member.service.MemberService;
import com.cpu.cams.point.PointConst;
import com.cpu.cams.point.dto.request.PointRequest;
import com.cpu.cams.point.entity.Point;
import com.cpu.cams.point.entity.PointType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.*;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AttendanceService {

    private final SessionRepository sessionRepository;
    private final ActivityParticipantRepository activityParticipantRepository;
    private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;
    private final ActivityRepository activityRepository;
    private final MemberService memberService;
    private final ActivityService activityService;
    private final SessionService sessionService;

    // 출석하기
    public Long attendance(Long sessionId, String attendancesCode, String username) {

        Member findMember = memberService.findByUsername(username);
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new CustomException(HttpStatus.FORBIDDEN, "권한이 없습니다."));

        if(session.getStatus().equals(SessionStatus.CLOSED)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "출석이 열리지 않았습니다.");
        }

        ActivityParticipant activityParticipant = activityParticipantRepository.findByMemberAndActivity(findMember, session.getActivity()).orElseThrow(() -> new RuntimeException("참여자 없음"));
        Attendance attendance = attendanceRepository.findBySessionAndParticipant(session, activityParticipant).orElseThrow(() -> new RuntimeException("없는 출석"));

        if(attendancesCode.equals(session.getAttendancesCode())){
            attendance.changeStatus(AttendanceStatus.PRESENT);
        } else {
            throw new CustomException(HttpStatus.BAD_REQUEST, "출석코드가 정확하지 않습니다.");
        }

        PointRequest pointRequest = PointRequest.builder()
                .amount(PointConst.ATTENDANCE_POINT)
                .type(PointType.ATTENDANCE.toString())
                .description(session.getActivity().getTitle() + " 활동" + session.getSessionNumber() + " 회차 출석 완료")
                .build();

        // 포인트 만들면서 멤버 포인트 증가까지 한번에 실행
        Point.create(pointRequest, findMember);

        return attendance.getId();

    }

    // 출석 ON/OFF 관리
    public Long updateAttendancesStatus(Long sessionId, Long participantId, String attendanceStatus, CustomUserDetails customUserDetails) {


        Attendance attendance = attendanceRepository.findBySessionIdAndParticipantId(sessionId, participantId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "출석을 찾을 수 없습니다."));

        // 팀장인지 확인하는 작업
        if(!attendance.getSession().getActivity().getCreatedBy().getUsername().equals(customUserDetails.getUsername()) && !checkAdmin(customUserDetails)){
            throw new CustomException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }

        attendance.changeStatus(AttendanceStatus.valueOf(attendanceStatus));

        return attendance.getId();
    }

    // 내 출결 조회
    public Page<ParticipantActivityAttendanceResponse> getMyAttendances(String username, int page, int size) {

        Member findMember = memberService.findByUsername(username);
        return attendanceRepository.findMyAttendances(findMember.getId(), PageRequest.of(page, size));
    }

    // 내가 개설한 활동의 출결 현황 조회
    //todo: 코드 고쳐야함
    public List<CreateActivityAttendanceResponse> getMyCreateActivityAttendances(String username) {

        Member findMember = memberService.findByUsername(username);
        List<Activity> activities = activityRepository.findByCreatedBy(findMember);

        List<CreateActivityAttendanceResponse> result = new ArrayList<>();
        List<CreateActivityAttendanceResponse.WeeklySummary> weeklySummaries = new ArrayList<>();
        List<CreateActivityAttendanceResponse.ParticipantSummary> participantSummaries = new ArrayList<>();
        Integer totalSession = 0;

        for (Activity activity : activities) {

            // 주차별 출석 현황
            List<Session> sessions = activity.getSessions();

            for (Session session : sessions) {
                totalSession++;

                List<Attendance> attendances = session.getAttendances();
                int PRESENT = 0;
                int LATE = 0;
                int ABSENT = 0;
                int sessionNum = session.getSessionNumber();

                for (Attendance attendance : attendances) {

                    if (attendance.getStatus() == AttendanceStatus.PRESENT){
                        PRESENT++;
                    }else if (attendance.getStatus() == AttendanceStatus.ABSENT){
                        ABSENT++;
                    }else if (attendance.getStatus().equals(AttendanceStatus.LATE)) {
                        LATE++;
                    }

                    CreateActivityAttendanceResponse.WeeklySummary summary = CreateActivityAttendanceResponse.WeeklySummary.builder()
                            .sessionNumber(sessionNum)
                            .attendanceCount(PRESENT)
                            .absentCount(ABSENT)
                            .lateCount(LATE)
                            .build();

                    weeklySummaries.add(summary);
                }
            }

            // 참여자 출석 현황
            List<ActivityParticipant> participants = activity.getParticipants();
            for (ActivityParticipant participant : participants) {
                List<Attendance> attendances = participant.getAttendances();
                int present = 0;
                int LATE = 0;
                int ABSENT = 0;
                String name = participant.getMember().getName();
                for (Attendance attendance : attendances) {

                    if (attendance.getStatus() == AttendanceStatus.PRESENT){
                        present++;
                    }else if (attendance.getStatus() == AttendanceStatus.ABSENT){
                        ABSENT++;
                    }else if (attendance.getStatus().equals(AttendanceStatus.LATE)) {
                        LATE++;
                    }

                }
                CreateActivityAttendanceResponse.ParticipantSummary summary = CreateActivityAttendanceResponse.ParticipantSummary.builder()
                        .memberId(participant.getMember().getId())
                        .name(name)
                        .attendanceCount(present)
                        .absentCount(ABSENT)
                        .lateCount(LATE)
                        .totalSessions(present + LATE + ABSENT)
                        .build();
                participantSummaries.add(summary);
            }

            CreateActivityAttendanceResponse response = CreateActivityAttendanceResponse.builder()
                    .activityId(activity.getId())
                    .activityTitle(activity.getTitle())
                    .totalSessions(totalSession)
                    .activityType(activity.getActivityType())
                    .participantSummaries(participantSummaries)
                    .weeklySummaries(weeklySummaries)
                    .build();

            result.add(response);
        }
        return result;
    }


    // 관리자인지 확인하는 로직
    private boolean checkAdmin(CustomUserDetails customUserDetails) {
        boolean isAdmin = customUserDetails.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority())); // 또는 Role.ROLE_ADMIN.name()

        if (!isAdmin) {
            throw new CustomException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }
        return true;
    }

    // 내가 개설한 특정 활동 출석 현황 조회
    public CreateActivityAttendanceResponse getAttendance(Long activityId, String username) {
        Member findMember = memberService.findByUsername(username);
        Activity activity = activityService.findById(activityId);

        if (!activity.getCreatedBy().equals(findMember)) {
            throw new CustomException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }

        List<CreateActivityAttendanceResponse.WeeklySummary> weeklySummaries = new ArrayList<>();
        List<CreateActivityAttendanceResponse.ParticipantSummary> participantSummaries = new ArrayList<>();
        Integer totalSession = 0;

        // 주차별 출석 현황
        List<Session> sessions = activity.getSessions();

        for (Session session : sessions) {
            totalSession++;

            List<Attendance> attendances = session.getAttendances();
            int PRESENT = 0;
            int LATE = 0;
            int ABSENT = 0;
            int sessionNum = session.getSessionNumber();

            for (Attendance attendance : attendances) {

                if (attendance.getStatus() == AttendanceStatus.PRESENT){
                    PRESENT++;
                }else if (attendance.getStatus() == AttendanceStatus.ABSENT){
                    ABSENT++;
                }else if (attendance.getStatus().equals(AttendanceStatus.LATE)) {
                    LATE++;
                }
            }
            CreateActivityAttendanceResponse.WeeklySummary summary = CreateActivityAttendanceResponse.WeeklySummary.builder()
                    .sessionNumber(sessionNum)
                    .attendanceCount(PRESENT)
                    .absentCount(ABSENT)
                    .lateCount(LATE)
                    .build();

            weeklySummaries.add(summary);
        }

        // 참여자 출석 현황
        List<ActivityParticipant> participants = activity.getParticipants();
        for (ActivityParticipant participant : participants) {
            List<Attendance> attendances = participant.getAttendances();
            int present = 0;
            int LATE = 0;
            int ABSENT = 0;
            String name = participant.getMember().getName();
            for (Attendance attendance : attendances) {

                if (attendance.getStatus() == AttendanceStatus.PRESENT){
                    present++;
                }else if (attendance.getStatus() == AttendanceStatus.ABSENT){
                    ABSENT++;
                }else if (attendance.getStatus().equals(AttendanceStatus.LATE)) {
                    LATE++;
                }

            }
            CreateActivityAttendanceResponse.ParticipantSummary summary = CreateActivityAttendanceResponse.ParticipantSummary.builder()
                    .memberId(participant.getMember().getId())
                    .name(name)
                    .attendanceCount(present)
                    .absentCount(ABSENT)
                    .lateCount(LATE)
                    .totalSessions(totalSession) // 전체 세션 수를 사용
                    .build();
            participantSummaries.add(summary);
        }

        return CreateActivityAttendanceResponse.builder()
                .activityId(activity.getId())
                .activityTitle(activity.getTitle())
                .totalSessions(totalSession)
                .activityType(activity.getActivityType())
                .participantSummaries(participantSummaries)
                .weeklySummaries(weeklySummaries)
                .build();
    }

    // 특정 세션 전체 출결 데이터 리스트 조회
    public List<SessionAttendanceResponse> getSessionAttendances(Long sessionId, String username) {
        // 1. 세션 조회
        Session session = sessionService.findById(sessionId);

        // 2. 활동 개설자 확인 (권한 체크)
        // 현재 로그인한 사용자가 해당 세션이 속한 활동의 개설자인지 확인
        if (!session.getActivity().getCreatedBy().getUsername().equals(username)) {
            throw new CustomException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }

        // 3. 해당 세션의 모든 출결 데이터 조회 (참가자 정보 포함)
        List<Attendance> attendances = attendanceRepository.findBySessionIdWithParticipantAndMember(sessionId);

        // 4. DTO로 변환
        return attendances.stream()
                .map(attendance -> new SessionAttendanceResponse(
                        attendance.getId(),
                        attendance.getStatus().name(), // Enum을 String으로 변환
                        String.valueOf(attendance.getParticipant().getMember().getId()), // Member ID
                        attendance.getParticipant().getMember().getName(), // Member Name
                        attendance.getParticipant().getMember().getDepartment()
                ))
                .collect(Collectors.toList());
    }

    // 특정 멤버의 특정 활동에 대한 전체 출결 데이터 리스트 조회
    public List<ParticipantActivityAttendanceResponse> getMemberActivityAttendances(Long activityId, Long memberId, String username) {
        Activity activity = activityService.findById(activityId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("멤버를 찾을 수 없습니다."));

        // 권한 확인: 활동 개설자 또는 관리자만 조회 가능
        if (!activity.getCreatedBy().getUsername().equals(username) && !memberService.checkAdmin(username)) {
            throw new CustomException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }

        ActivityParticipant activityParticipant = activityParticipantRepository.findByMemberAndActivity(member, activity)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "활동에 참여하지 않은 사용자입니다."));

        List<Session> sessions = sessionRepository.findByActivity(activity);

        List<ParticipantActivityAttendanceResponse> result = new ArrayList<>();

        for (Session session : sessions) {
            attendanceRepository.findBySessionAndParticipant(session, activityParticipant)
                    .ifPresent(attendance -> result.add(new ParticipantActivityAttendanceResponse(
                            attendance.getId(),
                            activity.getId(),
                            activity.getTitle(),
                            session.getSessionNumber(),
                            attendance.getStatus(),
                            attendance.getAttendanceTime()
                    )));
        }
        return result;
    }
}