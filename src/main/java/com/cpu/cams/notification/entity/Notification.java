//package com.cpu.cams.notification.entity;
//
//import com.cpu.cams.member.entity.Member;
//import jakarta.persistence.*;
//import org.hibernate.annotations.CreationTimestamp;
//
//import java.time.LocalDateTime;
//
//@Entity
//public class Notification {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id", nullable = false)
//    private Member member;
//
//    @Column()
//    private String message;
//
//    private Boolean isRead = false;
//
//    @Column(name = "created_at")
//    @CreationTimestamp
//    private LocalDateTime createdAt;
//}
