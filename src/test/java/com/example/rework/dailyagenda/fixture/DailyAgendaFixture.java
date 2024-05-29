package com.example.rework.dailyagenda.fixture;

import com.example.rework.dailyagenda.application.dto.DailyAgendaRequestDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DailyAgendaFixture {
    public static DailyAgendaRequestDto.CreateDailyAgendaRequestDto createAgenda() {
        return DailyAgendaRequestDto.CreateDailyAgendaRequestDto.builder()
                .todo("생성된 오늘의 아젠다")
                .createdAt(LocalDate.from(LocalDateTime.of(2024, 5, 29, 0, 0)))
                .pagingId(2L)
                .build();
    }

    public static DailyAgendaRequestDto.ReadDailyAgendaRequestDto readAgenda() {
        return DailyAgendaRequestDto.ReadDailyAgendaRequestDto.builder()
                .year(2024)
                .month(5)
                .day(29)
                .build();
    }

    public static DailyAgendaRequestDto.UpdateDailyAgendaRequestDto updateAgenda(Long agendaId) {
        return DailyAgendaRequestDto.UpdateDailyAgendaRequestDto.builder()
                .todo("수정한 아젠다")
                .agendaId(agendaId)
                .state(true)
                .build();
    }

    public static DailyAgendaRequestDto.ReadDailyCompleteRateRequestDto readDailyCompleteRate() {
        return DailyAgendaRequestDto.ReadDailyCompleteRateRequestDto.builder()
                .year(2024)
                .month(5)
                .day(29)
                .build();
    }

    public static DailyAgendaRequestDto.ReadMonthlyCompleteRateRequestDto readMonthlyCompleteRate() {
        return DailyAgendaRequestDto.ReadMonthlyCompleteRateRequestDto.builder()
                .year(2024)
                .month(5)
                .build();
    }

    public static List<DailyAgendaRequestDto.UpdateDailyAgendaListRequestDto> updateDailyAgendaListRequestDtoList(Long dailyAgendaId,Long updatePagingId) {
        return List.of(
                DailyAgendaRequestDto.UpdateDailyAgendaListRequestDto.builder()
                        .agendaId(dailyAgendaId)
                        .todo("오늘의 아젠다1")
                        .state(true)
                        .pagingId(updatePagingId)
                        .createdAt(LocalDate.from(LocalDateTime.of(2024, 5, 29, 0, 0)))
                        .build()
        );
    }
}
