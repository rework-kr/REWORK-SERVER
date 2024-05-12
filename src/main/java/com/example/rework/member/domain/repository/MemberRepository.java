package com.example.rework.member.domain.repository;

import com.example.rework.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByUserId(String userId);
    boolean existsByUserId(String userId);
}