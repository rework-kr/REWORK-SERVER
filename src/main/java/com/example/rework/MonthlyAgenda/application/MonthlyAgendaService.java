package com.example.rework.MonthlyAgenda.application;

import com.example.rework.MonthlyAgenda.application.dto.MonthlyAgendaRequestDto;
import com.example.rework.MonthlyAgenda.application.dto.MonthlyAgendaResponseDto;

public interface MonthlyAgendaService {
    MonthlyAgendaResponseDto.ReadMonthlyAgendaResponseDto readMonthlyAgenda(String date, Long currentUserId);

    MonthlyAgendaResponseDto.CreateMonthlyAgendaResponseDto createMonthlyAgenda(MonthlyAgendaRequestDto.CreateMonthlyAgendaRequestDto createMonthlyAgendaRequestDto, Long currentUserId);

    MonthlyAgendaResponseDto.UpdateMonthlyAgendaResponseDto updateMonthlyAgenda(MonthlyAgendaRequestDto.UpdateMonthlyAgendaRequestDto updateMonthlyAgendaRequestDto, Long currentUserId);

    boolean deleteMonthlyAgenda(Long monthlyAgendaId, Long currentUserId);
}
