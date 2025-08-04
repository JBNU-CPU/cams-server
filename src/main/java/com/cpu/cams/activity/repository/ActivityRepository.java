package com.cpu.cams.activity.repository;

import com.cpu.cams.activity.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
}
