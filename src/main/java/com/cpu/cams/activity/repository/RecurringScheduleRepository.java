package com.cpu.cams.activity.repository;

import com.cpu.cams.activity.entity.RecurringSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecurringScheduleRepository extends JpaRepository<RecurringSchedule, Long> {
}
