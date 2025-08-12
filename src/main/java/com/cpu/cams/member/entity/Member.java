package com.cpu.cams.member.entity;

import com.cpu.cams.activity.entity.Activity;
import com.cpu.cams.activity.entity.ActivityParticipant;
import com.cpu.cams.member.dto.request.SignupRequest;
import com.cpu.cams.point.entity.Point;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private Integer cohort;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Activity> createdActivities = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true) //todo: cascade 관련 문제 알아보기
    private List<ActivityParticipant> participatedActivities = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Point> pointList = new ArrayList<>();

    public static Member create(SignupRequest signupRequest) {
        Member member = new Member();
        member.username = signupRequest.getUsername();
        member.password = signupRequest.getPassword();
        member.name = signupRequest.getName();
        member.email = signupRequest.getEmail();
        member.phone = signupRequest.getPhone();
        member.department = signupRequest.getDepartment();
        member.role = Role.ROLE_ADMIN;
        member.cohort = signupRequest.getCohort();
        //todo: pointList 추가
        return member;
    }

    // 시큐리티 세션 등록 용 Member 객체 생성
    public static Member create(String username, String role) {
        Member member = new Member();
        member.username = username;
        member.role = Role.valueOf(role);
        return member;
    }


}
