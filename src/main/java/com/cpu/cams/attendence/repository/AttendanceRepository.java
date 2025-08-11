package com.cpu.cams.attendence.repository;

import com.cpu.cams.attendence.dto.response.ParticipantActivityAttendanceResponse;
import com.cpu.cams.attendence.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findBySessionIdAndParticipantId(Long sessionId, Long participantId);


    @Query(
            "select new com.cpu.cams.attendence.dto.response.ParticipantActivityAttendanceResponse(s.activity.title, s.sessionNumber, a.status, a.attendanceTime) " +
                    "from Member m " +
                    "join m.participatedActivities ap " +
                    "join ap.attendances a " +
                    "join a.session s " +
                    "where m.id = :memberId"
    )
    Page<ParticipantActivityAttendanceResponse> findMyAttendances(Long memberId, Pageable pageable);
}
