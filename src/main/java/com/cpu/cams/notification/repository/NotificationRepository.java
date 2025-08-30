package com.cpu.cams.notification.repository;

import com.cpu.cams.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
