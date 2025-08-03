package com.cpu.cams.activity.entity;

import com.cpu.cams.attendence.entity.Session;
import com.cpu.cams.user.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = true)
    private String goal;

    @Column(nullable = false)
    private ActivityType activityType;

    @Column(nullable = false)
    private Integer maxParticipants;

    @Column(nullable = false)
    private Integer participantCount;

    @Column(nullable = false)
    private String location;

    @Column(nullable = true)
    private String notes;

    @Column(nullable = false)
    private ActivityStatus activityStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;

    /* --- 관계 --- */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecurringSchedule> recurringSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventSchedule> eventSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions = new ArrayList<>();

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Curriculum> curriculums = new ArrayList<>(); // 커리큘럼 또는 프로그램
}
