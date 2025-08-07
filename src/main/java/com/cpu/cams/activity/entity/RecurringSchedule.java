package com.cpu.cams.activity.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity @Getter
public class RecurringSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recurring_schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Activity activity;

    private DayOfWeek weekday;
    private LocalTime startTime;
    private LocalTime endTime;
}
