package com.cpu.cams.activity.entity;

import jakarta.persistence.*;

@Entity
public class Curriculum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "curriculum_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    private Integer sequence; // 주차 혹은 식순

    private String title;

    private String description;
}
