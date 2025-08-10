package com.cpu.cams.activity.repository;

import com.cpu.cams.activity.entity.EventSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventScheduleRepository extends JpaRepository<EventSchedule, Long> {
}
