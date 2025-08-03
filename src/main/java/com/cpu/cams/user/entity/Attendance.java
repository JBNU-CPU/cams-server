package com.cpu.cams.user.entity;

import com.cpu.cams.activity.entity.ActivitySession;
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
    private ActivitySession session;

    @MapsId("participantId")
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "participant_id")
    private ActivityParticipant participant;

    @Enumerated(EnumType.STRING)
    private AttendanceState state;

    @CreationTimestamp
    private LocalDateTime checkedAt;
}
