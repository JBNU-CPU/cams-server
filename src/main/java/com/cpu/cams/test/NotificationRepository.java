package com.cpu.cams.test;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByUserIdAndReadAtIsNullOrderByCreatedAtAsc(Long userId);

    List<Notification> findTop50ByUserIdOrderByCreatedAtDesc(Long userId); // (선택) 최근 목록
}