package com.cpu.cams.member.repository;

import com.cpu.cams.member.entity.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {
    EmailAuth findByEmail(String email);
}
