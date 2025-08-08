package com.cpu.cams.activity.entity;

import com.cpu.cams.activity.dto.request.ActivityRequest;
import com.cpu.cams.attendence.entity.Session;
import com.cpu.cams.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    @Enumerated(EnumType.STRING)
    private ActivityType activityType;

    @Column(nullable = false)
    private Integer maxParticipants;

    @Column(nullable = false)
    private Integer participantCount = 0;

    @Column(nullable = false)
    private String location;

    @Column(nullable = true)
    private String notes;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActivityStatus activityStatus = ActivityStatus.NOT_STARTED;

    @CreationTimestamp
    private LocalDateTime createdAt;

    /* --- 관계 --- */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Member createdBy;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityParticipant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecurringSchedule> recurringSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventSchedule> eventSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions = new ArrayList<>();

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Curriculum> curriculums = new ArrayList<>(); // 커리큘럼 또는 프로그램

    public void addCreatedBy(Member member) {
        member.getCreatedActivities().add(this);
        this.createdBy = member;
    }


    public static Activity create(ActivityRequest activityRequest, Member member) {
        Activity activity = new Activity();
        activity.title = activityRequest.getTitle();
        activity.description = activityRequest.getDescription();
        activity.goal = activityRequest.getGoal();
        activity.activityType = activityRequest.getActivityType();
        activity.maxParticipants = activityRequest.getMaxParticipants();
        activity.location = activityRequest.getLocation();
        activity.notes = activityRequest.getNotes();
        activity.addCreatedBy(member);
        return activity;
    }

    public Activity updateActivity(ActivityRequest activityRequest) {
        this.title = activityRequest.getTitle();
        this.description = activityRequest.getDescription();
        this.goal = activityRequest.getGoal();
        this.activityType = activityRequest.getActivityType();
        this.maxParticipants = activityRequest.getMaxParticipants();
        this.location = activityRequest.getLocation();
        this.notes = activityRequest.getNotes();

        // todo: recurringSchedule, eventSchedule, curriculums 추가
        return this;
    }

    public Activity updateActivityStatus(String activityStatus) {
        ActivityStatus status = ActivityStatus.valueOf(activityStatus);
        this.activityStatus = status;
        return this;
    }
}


