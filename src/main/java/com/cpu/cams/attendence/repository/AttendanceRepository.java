package com.cpu.cams.attendence.repository;

import com.cpu.cams.activity.entity.Activity;
import com.cpu.cams.attendence.dto.response.CreateActivityAttendanceResponse;
import com.cpu.cams.attendence.dto.response.ParticipantActivityAttendanceResponse;
import com.cpu.cams.attendence.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findBySessionIdAndParticipantId(Long sessionId, Long participantId);

    //  내 출결 조회하기
    @Query(
            "select new com.cpu.cams.attendence.dto.response.ParticipantActivityAttendanceResponse(s.activity.title, s.sessionNumber, a.status, a.attendanceTime) " +
                    "from Member m " +
                    "join m.participatedActivities ap " +
                    "join ap.attendances a " +
                    "join a.session s " +
                    "where m.id = :memberId"
    )
    Page<ParticipantActivityAttendanceResponse> findMyAttendances(Long memberId, Pageable pageable);

    // 내가 개설한 활동 전체 출결 조회하기
//    @Query(
//            "select m.createdActivities from Member m " +
//                    "join m.participatedActivities pa " +
//                    "join pa.activity a " +
//                    "join a.sessions s " +
//                    "where m.id = :memberId"
//    )
//    Activity findMyCreateActivityAttendances(@Param("memberId") Long memberId, PageRequest of);
}
