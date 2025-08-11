package com.cpu.cams.attendence;

import com.cpu.cams.attendence.entity.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SessionRepository extends JpaRepository<Session, Long> {


    @Query("select s from Member m " +
            "join m.participatedActivities pa " +
            "join pa.activity ac " +
            "join ac.sessions s " +
            "where m.id = :memberId and " +
            "s.openAttendance = true")
    Page<Session> findOpenSessionList(@Param("memberId") Long memberId, PageRequest pageRequest);
}
