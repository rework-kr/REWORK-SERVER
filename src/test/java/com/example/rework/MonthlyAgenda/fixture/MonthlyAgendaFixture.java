package com.example.rework.MonthlyAgenda.fixture;

import com.example.rework.MonthlyAgenda.application.dto.MonthlyAgendaRequestDto;

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

    public static MonthlyAgendaRequestDto.ReadMonthlyAgendaRequestDto readAgenda(){
        return MonthlyAgendaRequestDto.ReadMonthlyAgendaRequestDto.builder()
                .year(2024)
                .month(5)
                .build();
    }

}
