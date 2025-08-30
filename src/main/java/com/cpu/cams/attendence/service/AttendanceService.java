package com.cpu.cams.attendence.service;

import com.cpu.cams.activity.entity.Activity;
import com.cpu.cams.activity.entity.ActivityParticipant;
import com.cpu.cams.activity.repository.ActivityParticipantRepository;
import com.cpu.cams.activity.repository.ActivityRepository;
import com.cpu.cams.attendence.dto.response.CreateActivityAttendanceResponse;
import com.cpu.cams.attendence.repository.SessionRepository;
import com.cpu.cams.attendence.dto.response.ParticipantActivityAttendanceResponse;
import com.cpu.cams.attendence.entity.Attendance;
import com.cpu.cams.attendence.entity.AttendanceStatus;
import com.cpu.cams.attendence.entity.Session;
import com.cpu.cams.attendence.repository.AttendanceRepository;
import com.cpu.cams.member.entity.Member;
import com.cpu.cams.member.repository.MemberRepository;
import com.cpu.cams.point.dto.request.PointRequest;
import com.cpu.cams.point.entity.Point;
import com.cpu.cams.point.entity.PointType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

    // 출석하기
    public Long attendance(Long sessionId, String attendancesCode) {
        // String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String username = "init2";
        Member findMember = memberRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("멤버없음"));
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new RuntimeException("없는 세션입니다."));
        ActivityParticipant activityParticipant = activityParticipantRepository.findByMemberAndActivity(findMember, session.getActivity()).orElseThrow(() -> new RuntimeException("참여자 없음"));
        Attendance attendance = attendanceRepository.findBySessionAndParticipant(session, activityParticipant).orElseThrow(() -> new RuntimeException("없는 출석"));

        if(!session.getOpenAttendance()){
            throw new RuntimeException("출석이 열리지 않았습니다.");
        }

        if(attendancesCode.equals(session.getAttendancesCode())){
            attendance.changeStatus(AttendanceStatus.PRESENT);
        } else {
            throw new RuntimeException("출석코드 틀림");
        }

        PointRequest pointRequest = new PointRequest();
        pointRequest.setAmount(10);
        pointRequest.setType(PointType.ATTENDANCE.toString());
        pointRequest.setDescription(session.getActivity().getTitle() + " 활동" + session.getSessionNumber() + " 회차 출석 완료");
        Point.create(pointRequest, findMember);
        findMember.updateTotalPoints(10);



        return attendance.getId();

    }

    public Long updateAttendancesStatus(Long sessionId, Long participantId, String attendanceStatus) {

        Attendance attendance = attendanceRepository.findBySessionIdAndParticipantId(sessionId, participantId).orElseThrow(() -> new RuntimeException("없는 출석"));
        attendance.changeStatus(AttendanceStatus.valueOf(attendanceStatus));

        return attendance.getId();
    }

    public Page<ParticipantActivityAttendanceResponse> getMyAttendances() {

        Long memberId = 2L;
        Page<ParticipantActivityAttendanceResponse> myAttendances = attendanceRepository.findMyAttendances(memberId, PageRequest.of(0, 10));
        return myAttendances;
    }

    //todo:
    public List<CreateActivityAttendanceResponse> getMyCreateActivityAttendances() {

        // String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String username = "init1";
        Member findMember = memberRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("멤버없음"));
        List<Activity> activities = activityRepository.findByCreatedBy(findMember);

        List<CreateActivityAttendanceResponse> result = new ArrayList<>();
        List<CreateActivityAttendanceResponse.WeeklySummary> weeklySummaries = new ArrayList<>();
        List<CreateActivityAttendanceResponse.ParticipantSummary> participantSummaries = new ArrayList<>();
        Integer totalSession = 0;

        for (Activity activity : activities) {
            List<Session> sessions = activity.getSessions();
            for (Session session : sessions) {
                totalSession++;

                List<Attendance> attendances = session.getAttendances();
                int present = 0;
                int LATE = 0;
                int ABSENT = 0;
                int sessionNum = session.getSessionNumber();

                for (Attendance attendance : attendances) {

                    if (attendance.getStatus() == AttendanceStatus.PRESENT){
                        present++;
                    }else if (attendance.getStatus() == AttendanceStatus.ABSENT){
                        ABSENT++;
                    }else if (attendance.getStatus().equals(AttendanceStatus.LATE)) {
                        LATE++;
                    }

                    CreateActivityAttendanceResponse.WeeklySummary summary = CreateActivityAttendanceResponse.WeeklySummary.builder()
                            .sessionNumber(sessionNum)
                            .attendanceCount(present)
                            .absentCount(ABSENT)
                            .lateCount(LATE)
                            .build();

                    weeklySummaries.add(summary);
                }
            }

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

            result.add(
                    CreateActivityAttendanceResponse.builder()
                    .activityId(activity.getId())
                    .activityTitle(activity.getTitle())
                    .totalSessions(totalSession)
                    .activityType(activity.getActivityType())
                    .participants(participantSummaries)
                    .weeklySummaries(weeklySummaries)
                    .build()
            );
        }
        return result;
    }
}
