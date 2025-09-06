package com.cpu.cams.attendence.entity;

import com.cpu.cams.activity.entity.Activity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_session_id")
    private Long id;

    @Column(nullable = false)
    private Integer sessionNumber; // 회차

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String attendancesCode; // 출석 코드

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus status; // 출석 열렸는지 확인

    @Column
    private LocalDateTime closedAt; // 마감 시간

    // == 연관관계 == //
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<Attendance> attendances = new ArrayList<>();

    // == 연관관계 편의 메서드 == //
    public void addActivity(Activity activity){
        this.activity = activity;
        activity.getSessions().add(this);
    }

    // == 생성자 메서드 == //
    public static Session create(Activity activity, int sessionNumber, String title, String attendancesCode, Integer closableAfterMinutes){
        Session session = new Session();
        session.addActivity(activity);
        session.sessionNumber = sessionNumber;
        session.title = title;
        session.attendancesCode = attendancesCode; // 클라이언트에서 생성되고 보내진 출석 코드
        session.status = SessionStatus.OPEN; // 세션 생성 시 바로 출석 오픈

        if (closableAfterMinutes != null) {
            session.closedAt = LocalDateTime.now().plusMinutes(closableAfterMinutes); //마감 시간 현재 시간 + 세션 만료 시간으로 설정
        }

        return session;
    }

    // == 비즈니스 로직 == //

    // 출석 마감 여부 변경
    public void setStatus(SessionStatus status){
        this.status = status;
    }

    // 출석 코드 변경
    public void changeCode(String attendancesCode){
        this.attendancesCode = attendancesCode;
    }

    // 출석 마감 기한 변경
    public void updateDeadline(LocalDateTime deadline) {
        if (deadline != null && deadline.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("마감 기한은 현재 시간 이후로만 설정할 수 있습니다.");
        }
        this.closedAt = deadline;
    }
}
