package com.example.rework.monthlyagenda.fixture;

import com.example.rework.monthlyagenda.application.dto.MonthlyAgendaRequestDto;

import java.time.LocalDateTime;

public class MonthlyAgendaFixture {
    public static MonthlyAgendaRequestDto.CreateMonthlyAgendaRequestDto createAgenda(){
        return  MonthlyAgendaRequestDto.CreateMonthlyAgendaRequestDto.builder()
                .todo("생성된 테스트 아젠다")
                .build();
    }

    public static MonthlyAgendaRequestDto.UpdateMonthlyAgendaRequestDto updateAgenda(Long agendaId){
        return MonthlyAgendaRequestDto.UpdateMonthlyAgendaRequestDto.builder()
                .agendaId(agendaId)
                .todo("수정된 테스트 아젠다")
                .build();
    }

}
