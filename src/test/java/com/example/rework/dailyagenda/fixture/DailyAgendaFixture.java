package com.example.rework.dailyagenda.fixture;

import com.example.rework.dailyagenda.application.dto.DailyAgendaRequestDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DailyAgendaFixture {
    static LocalDate Today = LocalDate.now();
    static int TEST_YEAR = Today.getYear();
    static int TEST_MONTH = Today.getMonthValue();
    static int TEST_DAY = Today.getDayOfMonth();

    public static DailyAgendaRequestDto.CreateDailyAgendaRequestDto createAgenda() {
        return DailyAgendaRequestDto.CreateDailyAgendaRequestDto.builder()
                .todo("생성된 오늘의 아젠다")
                .createdAt(LocalDate.from(LocalDateTime.of(TEST_YEAR, TEST_MONTH, TEST_DAY, 0, 0)))
                .pagingId(2L)
                .build();
    }

    public static DailyAgendaRequestDto.UpdateDailyAgendaRequestDto updateAgenda(Long agendaId) {
        return DailyAgendaRequestDto.UpdateDailyAgendaRequestDto.builder()
                .todo("수정한 아젠다")
                .agendaId(agendaId)
                .state(true)
                .build();
    }

    public static List<DailyAgendaRequestDto.UpdateDailyAgendaListRequestDto> updateDailyAgendaListRequestDtoList(Long dailyAgendaId, Long updatePagingId) {
        return List.of(
                DailyAgendaRequestDto.UpdateDailyAgendaListRequestDto.builder()
                        .agendaId(dailyAgendaId)
                        .todo("오늘의 아젠다1")
                        .state(true)
                        .pagingId(updatePagingId)
                        .createdAt(LocalDate.from(LocalDateTime.of(TEST_YEAR, TEST_MONTH, TEST_DAY, 0, 0)))
                        .build()
        );
    }
}
