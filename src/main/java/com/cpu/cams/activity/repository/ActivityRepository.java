package com.cpu.cams.activity.repository;

import com.cpu.cams.activity.entity.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ActivityRepository extends JpaRepository<Activity, Long> {

    // 전체 활동 조회
    Page<Activity> findAll(Pageable pageable);
}
