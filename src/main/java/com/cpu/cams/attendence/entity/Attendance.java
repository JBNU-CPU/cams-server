package com.cpu.cams.attendence.entity;

import com.cpu.cams.activity.entity.ActivityParticipant;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class Attendance {

    @EmbeddedId
    private AttendanceId id;

    /* 복합 PK의 FK 매핑 */
    @MapsId("sessionId")
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "session_id")
    private Session session;

    @MapsId("participantId")
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "participant_id")
    private ActivityParticipant participant;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus state;

    @CreationTimestamp
    private LocalDateTime attendanceTime;
}
