package com.cpu.cams.point.entity;

import com.cpu.cams.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private PointType type;

    private String description; // 생성 시 바로 활동 제목 문자열로 삽입

    private Integer amount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
