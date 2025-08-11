package com.cpu.cams.activity.repository;

import com.cpu.cams.activity.entity.Activity;
import com.cpu.cams.activity.entity.ActivityParticipant;
import com.cpu.cams.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityParticipantRepository extends JpaRepository<ActivityParticipant, Long> {
    
    // 액티비티의 참가자 목록 조회
    Page<ActivityParticipant> findByActivity(Activity activity, Pageable pageable);

    boolean existsByMember(Member member);
}
