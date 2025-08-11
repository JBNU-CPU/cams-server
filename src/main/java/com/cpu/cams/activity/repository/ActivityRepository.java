package com.cpu.cams.activity.repository;

import com.cpu.cams.activity.entity.Activity;
import com.cpu.cams.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ActivityRepository extends JpaRepository<Activity, Long> {

    // 전체 활동 조회
    Page<Activity> findAll(Pageable pageable);

    // 내가 개설한 활동 조회
    // todo: N+1 문제
    Page<Activity> findMyCreateActivityByCreatedBy(Pageable pageable, Member createdBy);

    // 내가 참가한 활동 조회
    @Query("""
    select ap.activity
    from ActivityParticipant ap
    where ap.member = :member
""")
    Page<Activity> findMyParticipateActivitiesByMember(@Param("member")Member member, Pageable pageable);


}
