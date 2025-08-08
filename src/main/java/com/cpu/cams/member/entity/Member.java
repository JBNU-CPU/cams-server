package com.cpu.cams.member.entity;

import com.cpu.cams.point.entity.Point;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private Integer cohort;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "member")
    private List<Point> pointList = new ArrayList<>();
}
