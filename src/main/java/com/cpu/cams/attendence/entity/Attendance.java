package com.cpu.cams.attendence.entity;

import com.cpu.cams.activity.entity.ActivityParticipant;
import com.cpu.cams.activity.entity.ActivityStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attendance {

    @EmbeddedId
    private AttendanceId id;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;

    @CreationTimestamp
    private LocalDateTime attendanceTime;

    /* 복합 PK의 FK 매핑 */
    @MapsId("sessionId")
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "session_id")
    private Session session;

    @MapsId("participantId")
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "participant_id")
    private ActivityParticipant participant;

    // == 연관관계 편의 메서드 == //
    public void creatPK(Session session, ActivityParticipant participant){
        this.session = session;
        session.getAttendances().add(this);

        this.participant = participant;
        participant.getAttendances().add(this);
    }

    // == 생성자 메서드 == //
    public static Attendance create(Session session, ActivityParticipant participant){
        Attendance attendance = new Attendance();
        attendance.id = new AttendanceId(session.getId(), participant.getId());
        attendance.creatPK(session, participant);
        attendance.status = AttendanceStatus.ABSENT; // 처음 생성 될때는 결석으로 처리

        return attendance;
    }

    // == 비즈니스 로직 == //
    public void changeStatus(AttendanceStatus status){
        this.status = status;
    }

}
