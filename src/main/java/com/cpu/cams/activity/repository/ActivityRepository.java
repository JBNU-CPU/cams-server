package com.cpu.cams.activity.repository;

import com.cpu.cams.activity.dto.response.ActivityResponse;
import com.cpu.cams.activity.entity.Activity;
import com.cpu.cams.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ActivityRepository extends JpaRepository<Activity, Long> {

    // 전체 활동 조회
    Page<Activity> findAll(Pageable pageable);

    // 내가 개설한 활동 조회
    // todo: N+1 문제
    Page<Activity> findMyBuildActivityByCreatedBy(Pageable pageable, Member createdBy);
}
