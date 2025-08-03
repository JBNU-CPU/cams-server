package com.cpu.cams.point.entity;

import com.cpu.cams.user.entity.User;
import jakarta.persistence.*;

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

    private String description;
    private Integer amount;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
}
