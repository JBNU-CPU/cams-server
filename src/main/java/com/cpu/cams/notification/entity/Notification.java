package com.cpu.cams.notification.entity;

import com.cpu.cams.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id", nullable = false)
//    private Member member;

    private String username;

    private String message;

    private Boolean isRead = false;
    private Boolean isSent = false;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    public static Notification create(String username, String message) {
        Notification notification = new Notification();
        notification.username = username;
        notification.message = message;
        return notification;
    }

    public void updateIsRead(boolean isRead) {
        this.isRead = isRead;
    }
}
