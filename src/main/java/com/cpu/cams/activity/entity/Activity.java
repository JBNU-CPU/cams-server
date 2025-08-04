package com.cpu.cams.activity.entity;

import com.cpu.cams.activity.dto.request.ActivityRequest;
import com.cpu.cams.attendence.entity.Session;
import com.cpu.cams.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
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
    private ActivityStatus activityStatus = ActivityStatus.NOT_STARTED;

    @CreationTimestamp
    private LocalDateTime createdAt;

    /* --- 관계 --- */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by") // todo: nullable = false
    private User createdBy;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecurringSchedule> recurringSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventSchedule> eventSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions = new ArrayList<>();

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Curriculum> curriculums = new ArrayList<>(); // 커리큘럼 또는 프로그램


    public static Activity createActivity(ActivityRequestDto activityRequestDto) {
        Activity activity = new Activity();
        activity.title = activityRequestDto.title;
        activity.description = activityRequestDto.description;
        activity.goal = activityRequestDto.goal;
        activity.activityType = activityRequestDto.activityType;
        activity.maxParticipants = activityRequestDto.maxParticipants;
        activity.location = activityRequestDto.location;
        activity.notes = activityRequestDto.notes;

        return activity;
    }

    @Builder
    public static class ActivityRequestDto {
        private String title;
        private String description;
        private String goal;
        private ActivityType activityType;
        private Integer maxParticipants;
        private String location;
        private String notes;
    }
}


