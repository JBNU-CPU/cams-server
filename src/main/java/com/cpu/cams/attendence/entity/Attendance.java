package com.cpu.cams.attendence.entity;

import com.cpu.cams.activity.entity.ActivityParticipant;
import com.cpu.cams.activity.entity.ActivityStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "attendance_uk",
                        columnNames = {"session_id", "participant_id"}
                )
        }
)
@Getter
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;

    @CreationTimestamp
    private LocalDateTime attendanceTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private ActivityParticipant participant;

    // == 연관관계 편의 메서드 == //
    public void creatPK(Session session, ActivityParticipant participant) {
        this.session = session;
        session.getAttendances().add(this);

        this.participant = participant;
        participant.getAttendances().add(this);
    }

    // == 생성자 메서드 == //
    public static Attendance create(Session session, ActivityParticipant participant) {
        Attendance attendance = new Attendance();
        attendance.creatPK(session, participant);
        attendance.status = AttendanceStatus.ABSENT; // 처음 생성 될때는 결석으로 처리

        return attendance;
    }

    // == 출석 상태 변경 로직 == //
    public void changeStatus(AttendanceStatus status) {
        this.status = status;
    }

}
