package com.example.rework.MonthlyAgenda.domain.repository;

import com.example.rework.MonthlyAgenda.domain.MonthlyAgenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MonthlyAgendaRepository extends JpaRepository<MonthlyAgenda, Long> {
    Optional<MonthlyAgenda> findByMemberIdAndCreatedAtBetween(Long memberId, LocalDateTime start, LocalDateTime end);
}
