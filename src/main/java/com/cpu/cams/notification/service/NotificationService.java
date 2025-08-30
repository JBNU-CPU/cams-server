package com.cpu.cams.notification.service;

import com.cpu.cams.notification.entity.Notification;
import com.cpu.cams.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // 알림 생성
    @Transactional
    public void createNotification(String username, String message) {
        Notification notification = Notification.create(username, message);
        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<Notification> getPendingNotifications(String username) {
        // 아직 읽지 않았고, 아직 전송되지 않은 것만
        return notificationRepository.findByUsernameAndIsReadAndIsSent(username, false, false);
    }

    // 알림이 전송될 때 상태 변경
    @Transactional
    public void markAsSent(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("알림을 찾을 수 없습니다."));
        notification.updateIsSent(true);
    }

    @Transactional
    public void markAsRead(Long notificationId, String username) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("알림을 찾을 수 없습니다."));
        if (!username.equals(notification.getUsername())) {
            throw new RuntimeException("권한이 없습니다.");
        }
        notification.updateIsSent(true);
    }

    // 특정 ID로 알림을 가져옴
    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("알림을 찾을 수 없습니다."));
    }

    // 사용자에게 보낼 알림 목록을 반환
    public List<Notification> getUnreadNotificationsByUsername(String username) {
        return notificationRepository.findByUsernameAndIsRead(username, false);
    }
}
