package com.cpu.cams.attendence.service;

import com.cpu.cams.activity.entity.Activity;
import com.cpu.cams.activity.repository.ActivityRepository;
import com.cpu.cams.attendence.dto.request.SessionRequest;
import com.cpu.cams.attendence.dto.response.OpenSessionResponse;
import com.cpu.cams.attendence.dto.response.SessionInfoResponse;
import com.cpu.cams.attendence.entity.Attendance;
import com.cpu.cams.attendence.entity.Session;
import com.cpu.cams.attendence.entity.SessionStatus;
import com.cpu.cams.attendence.repository.SessionRepository;
import com.cpu.cams.member.entity.Member;
import com.cpu.cams.member.repository.MemberRepository;
import com.cpu.cams.notification.NotificationPayload;
import com.cpu.cams.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SessionService {

    private final ActivityRepository activityRepository;
    private final SessionRepository sessionRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    // 세션 만들기
    public Long createSession(Long activityId, SessionRequest request, String username) {

        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("해당 활동을 찾을 수 없습니다."));

        if (!activity.getCreatedBy().getUsername().equals(username)) {
            throw new RuntimeException("세션을 생성할 권한이 없습니다.");
        }

        // 1. 이미 열린 세션이 있는지 확인
        boolean hasOpen = activity.getSessions().stream().anyMatch(session -> session.getStatus() == SessionStatus.OPEN);
        if (hasOpen) {
            throw new RuntimeException("이미 진행 중인 세션이 있습니다. 기존 세션을 먼저 닫아주세요.");
        }

        // 2. 세션 생성
        Session session = Session.create(activity, activity.getSessionCount() + 1, activity.getTitle(), request.getAttendanceCode(), request.getClosableAfterMinutes());
        Session saveSession = sessionRepository.save(session);

        // 3. 세션에 따른 모든 참여 학생의 출석(Attendance) 객체 생성 (초기값: 결석)
        // 학생에 따른 출석부 만들기 -> 더티채킹으로 repository 없이 만들기
        activity.getParticipants().forEach(participant -> Attendance.create(saveSession, participant));

        // 4. Activity 세션 수 증가
        activity.addSession();

        // 5. 활동 참가자들에게 알림
        NotificationPayload notificationPayload = new NotificationPayload("출석 열림", activity.getTitle() + " 출석이 열렸습니다.", "~~~링크 참조");
        activity.getParticipants().forEach( activityParticipant -> {
            notificationService.createAndSend(activityParticipant.getMember().getId(), notificationPayload);
        });

        return session.getId();
    }

    // 열린 세션 목록 조회
    @Transactional(readOnly = true)
    public Page<OpenSessionResponse> findOpenSessionList(String username, int page, int size) {

        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // memberId -> activity_partipants에서 activity조회해서 그 중에 session이 start인걸 조회한다~ 리턴값으로는 activity, session 둘 다 줘야 한다.
        // 1. memberId로 activity 찾기
        // 2. activity들 session 찾기
        // 3. session의 openAttendance true인 것 찾기
        return sessionRepository.findOpenSessionList(member.getId(), PageRequest.of(page, size)).map(
                session -> OpenSessionResponse.builder()
                        .sessionId(session.getId())
                        .activityTitle(session.getActivity().getTitle())
                        .sessionNumber(session.getSessionNumber())
                        .closedAt(session.getClosedAt())
                        .createdBy(session.getActivity().getCreatedBy().getName())
                        .build());
    }

    // (수동) 출석 마감
    public Long closeSession(Long sessionId, String username) {

        Session session = isOwner(username, sessionId);

        if (session.getStatus() == SessionStatus.CLOSED) {
            throw new RuntimeException("이미 닫힌 세션입니다.");
        }

        session.setStatus(SessionStatus.CLOSED);

        return session.getId();
    }

    // 세션 정보 변경 (출석코드, 마감기한)
    @Transactional
    public Long updateSessionInfo(Long sessionId, SessionRequest request, String username) {
        Session session = isOwner(username, sessionId);

        if (request.getAttendanceCode() != null && !request.getAttendanceCode().isEmpty()) {
            session.changeCode(request.getAttendanceCode());
        }

        if (request.getClosableAfterMinutes() != null) {
            if (request.getClosableAfterMinutes() <= 0) {
                throw new IllegalArgumentException("마감 시간은 0보다 커야 합니다.");
            }
            LocalDateTime newDeadline = LocalDateTime.now().plusMinutes(request.getClosableAfterMinutes());
            session.updateDeadline(newDeadline);
        }

        return session.getId();
    }

    // 해당 활동의 모든 세션 조회
    @Transactional(readOnly = true)
    public Page<SessionInfoResponse> findSessionsByActivity(Long activityId, int page, int size) {
        if (!activityRepository.existsById(activityId)) {
            throw new IllegalArgumentException("해당 활동을 찾을 수 없습니다.");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("sessionNumber").descending());
        Page<Session> sessions = sessionRepository.findByActivityId(activityId, pageable);
        return sessions.map(SessionInfoResponse::from);
    }

    // 세션 자동 마감 스케줄러 (매 분 0초에 실행)
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void autoCloseSessions() {
        log.info("Checking for sessions to auto-close at {}", LocalDateTime.now());
        List<Session> sessionsToClose = sessionRepository.findByStatusAndClosedAtBefore(SessionStatus.OPEN, LocalDateTime.now());

        if (!sessionsToClose.isEmpty()) {
            log.info("[Auto-Close] Found {} sessions to close.", sessionsToClose.size());
            for (Session session : sessionsToClose) {
                session.setStatus(SessionStatus.CLOSED);
                log.info("  - Closing session ID: {} for activity: {}", session.getId(), session.getActivity().getTitle());
            }
        }
    }

    // 3분 지난 세션 FINISHED로 변경
    @Scheduled(cron = "0 * * * * *") // 1분마다 실행
    @Transactional
    public void autoFinishSessions() {
        log.info("Checking for sessions to auto-finish at {}", LocalDateTime.now());
        LocalDateTime finishedDuration = LocalDateTime.now().minusMinutes(3);
        //LocalDateTime finishedDuration = LocalDateTime.now().minusHours(3); // todo: 3시간으로 변경해야 함!
        List<Session> sessionsToFinish = sessionRepository.findByStatusNotAndCreatedAtBefore(SessionStatus.FINISHED, finishedDuration);

        if (!sessionsToFinish.isEmpty()) {
            log.info("[Auto-Finish] Found {} sessions to finish.", sessionsToFinish.size());
            for (Session session : sessionsToFinish) {
                session.setStatus(SessionStatus.FINISHED);
                log.info("  - Finishing session ID: {} for activity: {}", session.getId(), session.getActivity().getTitle());
            }
        }
    }

    // 세션 소유자 확인 메서드
    private Session isOwner(String username, Long sessionId) {
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new RuntimeException("세션을 찾을 수 없습니다."));

        if (!session.getActivity().getCreatedBy().getUsername().equals(username)) {
            throw new RuntimeException("세션에 대한 권한이 없습니다.");
        }
        return session;
    }
}
