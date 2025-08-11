package com.cpu.cams.attendence.entity;

import com.cpu.cams.activity.entity.Activity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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
    private String description;

    @Column(nullable = false)
    private String attendancesCode; // 출석 코드

    @Column(nullable = false)
    private Boolean isDone; // 출석 마감 여부

    // == 연관관계 == //
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @OneToMany(mappedBy = "session")
    private List<Attendance> attendances;

    // == 연관관계 편의 메서드 == //
    public void addActivity(Activity activity){
        this.activity = activity;
        activity.getSessions().add(this);
    }

    // == 생성자 메서드 == //
    public static Session create(Activity activity, int sessionNumber, String description, String attendancesCode){
        Session session = new Session();
        session.addActivity(activity);
        session.sessionNumber = sessionNumber;
        session.description = description;
        session.attendancesCode = attendancesCode;
        session.isDone = false;

        return session;
    }

    // == 비즈니스 로직 == //
    // 출석 마감 여부 변경
    public void toggleDoneStatus(){
        this.isDone = !this.isDone;
    }

    public void changeCode(String attendancesCode){
        this.attendancesCode = attendancesCode;
    }



}
