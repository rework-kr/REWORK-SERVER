package com.example.rework.member.domain.repository;

import com.example.rework.member.domain.NonMemberEmail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface NonMemberRepository extends JpaRepository<NonMemberEmail,Long> {
     List<NonMemberEmail> findAllByIsAccepted(boolean isAccepted);
}