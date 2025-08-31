package com.cpu.cams.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // 읽지 않은 것
    List<Notification> findAllByUserIdAndReadAtIsNullOrderByCreatedAtAsc(Long userId);

    // lastId기반
    List<Notification> findAllByUserIdAndIdGreaterThanOrderByIdAsc(Long userId, Long lastId);

    List<Notification> findTop50ByUserIdOrderByCreatedAtDesc(Long userId); // (선택) 최근 목록

}