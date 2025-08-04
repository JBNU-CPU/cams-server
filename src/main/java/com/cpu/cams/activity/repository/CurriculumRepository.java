package com.cpu.cams.activity.repository;

import com.cpu.cams.activity.entity.Curriculum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurriculumRepository extends JpaRepository<Curriculum, Long> {
}
