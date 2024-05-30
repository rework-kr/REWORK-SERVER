package com.example.rework.monthlyagenda.domain.repository;

import com.example.rework.monthlyagenda.domain.MonthlyAgenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MonthlyAgendaRepository extends JpaRepository<MonthlyAgenda, Long> {
    Optional<MonthlyAgenda> findByMemberIdAndCreatedAtBetween(Long memberId, LocalDateTime start, LocalDateTime end);
}
