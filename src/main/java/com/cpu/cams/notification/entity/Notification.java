package com.cpu.cams.notification.entity;

import com.cpu.cams.user.entity.User;
import jakarta.persistence.*;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT") private String message;
    private Boolean isRead = Boolean.FALSE;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
}
