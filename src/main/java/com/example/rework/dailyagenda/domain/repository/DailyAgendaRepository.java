package com.example.rework.dailyagenda.domain.repository;

import com.example.rework.dailyagenda.domain.DailyAgenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DailyAgendaRepository extends JpaRepository<DailyAgenda, Long> {
    List<DailyAgenda> findByMemberIdAndCreatedAtBetween(Long memberId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<DailyAgenda> findByMemberIdAndCreatedAtBetweenAndState(Long memberId, LocalDateTime startOfDay, LocalDateTime endOfDay, boolean state);

}
