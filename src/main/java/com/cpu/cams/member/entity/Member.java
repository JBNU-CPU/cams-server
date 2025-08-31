package com.cpu.cams.member.entity;

import com.cpu.cams.activity.entity.Activity;
import com.cpu.cams.activity.entity.ActivityParticipant;
import com.cpu.cams.announcement.entity.Announcement;
import com.cpu.cams.member.dto.request.ProfileRequest;
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

    @Column
    private Integer totalPoints = 0;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private Integer cohort;

    @Column
    private String introduce; // 자기소개

    @ElementCollection(fetch = FetchType.LAZY) // EAGER 로딩보다 LAZY 로딩이 권장됩니다.
    @CollectionTable(name = "member_interesting", // 생성될 테이블의 이름
            joinColumns = @JoinColumn(name = "member_id") // Member 테이블과 조인할 외래 키
    )
    @Column(name = "interesting_name") // 컬렉션의 값이 저장될 컬럼 이름
    @Enumerated(EnumType.STRING) // Enum의 이름을 String으로 저장 (e.g., "BACKEND")
    private List<Interesting> interesting = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Activity> createdActivities = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true) //todo: cascade 관련 문제 알아보기
    private List<ActivityParticipant> participatedActivities = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Point> pointList = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Announcement> announcementList = new ArrayList<>();

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
        return member;
    }

    // 시큐리티 세션 등록 용 Member 객체 생성
    public static Member create(String username, String role) {
        Member member = new Member();
        member.username = username;
        member.role = Role.valueOf(role);
        return member;
    }
    
    // 프로필 수정
    public Member update(ProfileRequest profileRequest) {
        this.name = profileRequest.getName();
        this.email = profileRequest.getEmail();
        this.phone = profileRequest.getPhone();
        this.department = profileRequest.getDepartment();
        this.cohort = profileRequest.getCohort();
        this.introduce = profileRequest.getIntroduce();

        List<String> interesting1 = profileRequest.getInteresting();
        this.interesting = interesting1.stream().map(i -> Interesting.valueOf(i)).toList();

        return this;
    }

    // 멤버 권한 변경
    public Role updateMemberRole(String role) {
        this.role = Role.valueOf(role);
        return this.role;
    }

    public void updateTotalPoints(Integer pointAmount) {
        if (this.totalPoints + pointAmount < 0) {
            throw new RuntimeException("포인트가 이상해요");
        }
        this.totalPoints = this.totalPoints + pointAmount;
    }

}
