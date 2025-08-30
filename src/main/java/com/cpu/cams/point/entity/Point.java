package com.cpu.cams.point.entity;

import com.cpu.cams.member.entity.Member;
import com.cpu.cams.point.dto.request.PointRequest;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private PointType type;

    private String description; // 생성 시 바로 활동 제목 문자열로 삽입

    private Integer amount;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    public static Point create(PointRequest pointRequest, Member member) {
        Point point = new Point();
        point.type = PointType.valueOf(pointRequest.getType());
        point.description = pointRequest.getDescription();
        point.amount = pointRequest.getAmount();
        point.addMember(member);
        return point;
    }

    private void addMember(Member member) {
        member.getPointList().add(this);
        this.member = member;
    }
}
