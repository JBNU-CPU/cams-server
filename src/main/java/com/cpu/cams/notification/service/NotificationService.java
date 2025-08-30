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

    // 알림을 생성
    @Transactional
    public void createNotification(String username, String message) {
        Notification notification = Notification.create(username, message);
        notificationRepository.save(notification);
    }

    // 알림 리스트 목록에서 버튼을 누르면 알림을 읽은 상태로 표시
    public void markAsRead(Notification notification) {
        notification.updateIsRead(true);
        notificationRepository.save(notification);
    }

    // 알림이 보낼 때 상태
    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("알림을 찾을 수 없습니다."));
        notification.updateIsRead(true);
        notificationRepository.save(notification);
    }

    // 특정 ID로 알림을 가져옴
    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("알림을 찾을 수 없습니다."));
    }

    // 사용자에게 보낸 알림 목록을 반환
    public List<Notification> getUnreadNotificationsByUsername(String username) {
        return notificationRepository.findByUsernameAndIsRead(username, false);
    }
}
