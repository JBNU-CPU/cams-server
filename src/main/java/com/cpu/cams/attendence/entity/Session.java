package com.cpu.cams.attendence.entity;

import com.cpu.cams.activity.entity.Activity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.List;

@Entity
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
    private String attendancesCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @OneToMany(mappedBy = "session")
    private List<Attendance> attendances;
}
