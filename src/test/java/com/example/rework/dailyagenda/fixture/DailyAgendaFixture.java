package com.example.rework.dailyagenda.fixture;

import com.example.rework.dailyagenda.application.dto.DailyAgendaRequestDto;

public class DailyAgendaFixture {
    public static DailyAgendaRequestDto.CreateDailyAgendaRequestDto createAgenda() {
        return DailyAgendaRequestDto.CreateDailyAgendaRequestDto.builder()
                .todo("생성된 오늘의 아젠다")
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
}
