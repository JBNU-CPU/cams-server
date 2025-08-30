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
}
