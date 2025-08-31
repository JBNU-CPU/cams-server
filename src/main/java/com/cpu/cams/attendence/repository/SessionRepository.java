package com.cpu.cams.attendence.repository;

import com.cpu.cams.attendence.entity.Session;
import com.cpu.cams.attendence.entity.SessionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {


    @Query("select s from Member m " +
            "join m.participatedActivities pa " +
            "join pa.activity ac " +
            "join ac.sessions s " +
            "left join Attendance a on a.session = s and a.participant = pa " +
            "where m.id = :memberId and " +
            "s.status = 'OPEN' and " +
            "(a.status is null or a.status = 'ABSENT')")
    Page<Session> findOpenSessionList(@Param("memberId") Long memberId, PageRequest pageRequest);

    List<Session> findByStatusAndClosedAtBefore(SessionStatus status, LocalDateTime time);
}
