package com.cpu.cams.member.repository;

import com.cpu.cams.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    // id만 가져오면 더 가벼움
    @Query("select m.id from Member m where m.username = :username")
    Optional<Long> findIdByUsername(@Param("username") String username);
}
