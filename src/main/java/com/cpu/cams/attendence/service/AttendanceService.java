package com.cpu.cams.attendence.service;

import com.cpu.cams.activity.repository.ActivityParticipantRepository;
import com.cpu.cams.attendence.repository.SessionRepository;
import com.cpu.cams.attendence.dto.response.ParticipantActivityAttendanceResponse;
import com.cpu.cams.attendence.entity.Attendance;
import com.cpu.cams.attendence.entity.AttendanceStatus;
import com.cpu.cams.attendence.entity.Session;
import com.cpu.cams.attendence.repository.AttendanceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class AttendanceService {

    private final SessionRepository sessionRepository;
    private final ActivityParticipantRepository activityParticipantRepository;
    private final AttendanceRepository attendanceRepository;

    public Long attendance(Long sessionId, Long participantId, String attendancesCode) {

        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new RuntimeException("없는 세션입니다."));
        Attendance attendance = attendanceRepository.findBySessionIdAndParticipantId(sessionId, participantId).orElseThrow(() -> new RuntimeException("없는 출석"));

        if(attendancesCode.equals(session.getAttendancesCode())){
            attendance.changeStatus(AttendanceStatus.PRESENT);
        } else {
            throw new RuntimeException("출석코드 틀림");
        }

        return attendance.getId();

    }

    public Long updateAttendancesStatus(Long sessionId, Long participantId, String attendanceStatus) {

        Attendance attendance = attendanceRepository.findBySessionIdAndParticipantId(sessionId, participantId).orElseThrow(() -> new RuntimeException("없는 출석"));
        attendance.changeStatus(AttendanceStatus.valueOf(attendanceStatus));

        return attendance.getId();
    }

    public Page<ParticipantActivityAttendanceResponse> getMyAttences() {

        Long memberId = 2L;
        Page<ParticipantActivityAttendanceResponse> myAttendances = attendanceRepository.findMyAttendances(memberId, PageRequest.of(0, 10));
        return myAttendances;
    }
}
