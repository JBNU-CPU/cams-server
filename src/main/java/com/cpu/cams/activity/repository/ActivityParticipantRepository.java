package com.cpu.cams.activity.repository;

import com.cpu.cams.activity.entity.ActivityParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityParticipantRepository extends JpaRepository<ActivityParticipant, Long> {
}
