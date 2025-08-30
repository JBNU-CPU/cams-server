package com.cpu.cams.attendence.service;

import com.cpu.cams.activity.entity.Activity;
import com.cpu.cams.activity.repository.ActivityRepository;
import com.cpu.cams.attendence.repository.SessionRepository;
import com.cpu.cams.attendence.dto.request.SessionRequest;
import com.cpu.cams.attendence.dto.response.OpenSessionResponse;
import com.cpu.cams.attendence.entity.Attendance;
import com.cpu.cams.attendence.entity.Session;
import com.cpu.cams.member.entity.Member;
import com.cpu.cams.member.repository.MemberRepository;
import com.cpu.cams.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SessionService {

    private final ActivityRepository activityRepository;
    private final SessionRepository sessionRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    // 세션 만들기
    public Long createSession(Long activityId, SessionRequest request, String username) {

        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("활동 없는데?"));

        if(!activity.getCreatedBy().getUsername().equals(username)){
            throw new RuntimeException("너 누구야?!");
        }
        // 세션 만들기
        
        // 1. 기존 세션 있는지 확인
        boolean hasOpen = activity.getSessions().stream().anyMatch(session -> Boolean.TRUE.equals(session.getOpenAttendance()));
        if(hasOpen){
            throw new RuntimeException("이미 세션 존재함");
        }
        // 2. 기존 세션 없다면 만들기
        Session session = Session.create(activity, request.getSessionNumber(), request.getDescription(), request.getAttendanceCode());
        Session saveSession = sessionRepository.save(session);

        // 세션에 따른 모든 참여 학생 attendance 객체 생성 (초기값 : 결석)
        // 학생에 따른 출석부 만들기 -> 더티채킹으로 repository 없이 만들기
        activity.getParticipants().forEach(participant -> Attendance.create(saveSession, participant));

        return session.getId();
    }

    // 열린 세션 확인하기
    public Page<OpenSessionResponse> findOpenSessionList(String username, int page, int size) {

        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("유저가 없어요"));

        // memberId -> activity_partipants에서 activity조회해서 그 중에 session이 start인걸 조회한다~ 리턴값으로는 activity, session 둘 다 줘야 한다.
        // 1. memberId로 activity 찾기
        // 2. activity들 session 찾기
        // 3. session의 openAttendance true인 것 찾기
        return sessionRepository.findOpenSessionList(member.getId(), PageRequest.of(page, size)).map(
                session -> OpenSessionResponse.builder()
                            .sessionId(session.getId())
                            .activityTitle(session.getActivity().getTitle())
                            .sessionNumber(session.getSessionNumber())
                            .build());
    }

    // 출석 마감 여부 수정 -> 세션 상태 변경
    public Long toggleOpenAttendance(Long sessionId, String username) {

        Session session = isOwner(username, sessionId);

        session.toggleDoneStatus();

        return session.getId();
    }

    // 출석 코드 변경
    public Long updateAttendanceCode(Long sessionId, String attendanceCode, String username) {
        
        Session session = isOwner(username, sessionId);

        session.changeCode(attendanceCode);
        return session.getId();
    }

    // 세션 주인 확인 메서드
    private Session isOwner(String username, Long sessionId) {
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new RuntimeException("세션이 없어요"));
        
        if(!session.getActivity().getCreatedBy().getUsername().equals(username)){
            throw new RuntimeException("너 누구야?!");
        }
        return session;
    }


}
