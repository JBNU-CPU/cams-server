package com.cpu.cams.attendence.repository;

import com.cpu.cams.attendence.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findBySessionIdAndParticipantId(Long sessionId, Long participantId);
}
