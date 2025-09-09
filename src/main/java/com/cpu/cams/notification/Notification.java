//package com.cpu.cams.notification;
//
//import jakarta.persistence.*;
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import java.time.LocalDateTime;
//
//@Entity @Getter
//@EntityListeners(AuditingEntityListener.class)
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class Notification {
//
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private Long userId;
//
//    @Column(nullable = false, length = 30)
//    private String type;
//
//    @Column(nullable = false, length = 500)
//    private String message;
//
//    @Column(length = 255)
//    private String link;
//
//    @CreatedDate
//    @Column(updatable = false)
//    private LocalDateTime createdAt;
//
//    private LocalDateTime readAt;
//
//    public static Notification create(Long userId, String type, String message, String link) {
//        Notification n = new Notification();
//        n.userId = userId;
//        n.type = type;
//        n.message = message;
//        n.link = link;
//        return n;
//    }
//
//    public void markRead() {
//        this.readAt = LocalDateTime.now();
//    }
//
//    public boolean isUnread() {
//        return readAt == null;
//    }
//}
