package com.cpu.cams.attendence.service;

import com.cpu.cams.activity.entity.Activity;
import com.cpu.cams.activity.repository.ActivityRepository;
import com.cpu.cams.attendence.SessionRepository;
import com.cpu.cams.attendence.dto.request.SessionRequest;
import com.cpu.cams.attendence.dto.response.OpenSessionResponse;
import com.cpu.cams.attendence.entity.Attendance;
import com.cpu.cams.attendence.entity.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SessionService {

    private final ActivityRepository activityRepository;
    private final SessionRepository sessionRepository;

    @Transactional
    public Long createSession(Long activityId, SessionRequest request) {

        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("활동 없는데?"));
        // 세션 만들기
        Session session = Session.create(activity, request.getSessionNumber(), request.getDescription(), request.getAttendanceCode());

        Session saveSession = sessionRepository.save(session);

        // 세션에 따른 모든 참여 학생 attendance 객체 생성
        // 학생에 따른 출석부 만들기 -> 더티채킹으로 repository 없이 만들기
        List<Attendance> attendanceList = activity.getParticipants().stream().map(participant -> Attendance.create(saveSession, participant)).toList();

        return session.getId();
    }

    // 열린 세션 확인하기
    public Page<OpenSessionResponse> findOpenSessionList(Long memberId) {
        // memberId -> activity_partipants에서 activity조회해서 그 중에 session이 start인걸 조회한다~ 리턴값으로는 activity, session 둘 다 줘야 한다.
        // 1. memberId로 activity 찾기
        // 2. activity들 session 찾기
        // 3. session의 openAttendance true인 것 찾기
        PageRequest pageRequest = PageRequest.of(0, 3);
        Page<OpenSessionResponse> openSessionList = sessionRepository.findOpenSessionList(memberId, pageRequest).map(
                session -> OpenSessionResponse.builder()
                            .sessionId(session.getId())
                            .activityTitle(session.getActivity().getTitle())
                            .sessionNumber(session.getSessionNumber())
                            .build());
        return openSessionList;
    }
    
    // 출석하기
}
