package com.cpu.cams.notification.entity;

import com.cpu.cams.member.entity.Member;
import jakarta.persistence.*;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(columnDefinition = "TEXT") private String message;
    private Boolean isRead = Boolean.FALSE;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
}
