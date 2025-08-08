package com.cpu.cams.member.entity;

import com.cpu.cams.member.dto.request.SignupRequest;
import com.cpu.cams.point.entity.Point;
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

    @OneToMany(mappedBy = "member")
    private List<Point> pointList = new ArrayList<>();

    public static Member create(SignupRequest signupRequest) {
        Member member = new Member();
        member.username = signupRequest.getUsername();
        member.password = signupRequest.getPassword();
        member.name = signupRequest.getName();
        member.email = signupRequest.getEmail();
        member.phone = signupRequest.getPhone();
        member.department = signupRequest.getDepartment();
        member.role = Role.ROLE_USER;
        member.cohort = signupRequest.getCohort();
        //todo: pointList 추가
        return member;
    }
}
