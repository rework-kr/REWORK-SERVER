package com.example.rework.monthlyagenda.application;

import com.example.rework.monthlyagenda.application.dto.MonthlyAgendaRequestDto;
import com.example.rework.monthlyagenda.application.dto.MonthlyAgendaResponseDto;
import com.example.rework.config.security.SecurityUtils;

public interface MonthlyAgendaService {
    MonthlyAgendaResponseDto.ReadMonthlyAgendaResponseDto readMonthlyAgenda(int year, int month, SecurityUtils securityUtils);

    MonthlyAgendaResponseDto.CreateMonthlyAgendaResponseDto createMonthlyAgenda(MonthlyAgendaRequestDto.CreateMonthlyAgendaRequestDto createMonthlyAgendaRequestDto, SecurityUtils securityUtils);

    MonthlyAgendaResponseDto.UpdateMonthlyAgendaResponseDto updateMonthlyAgenda(MonthlyAgendaRequestDto.UpdateMonthlyAgendaRequestDto updateMonthlyAgendaRequestDto, SecurityUtils securityUtils);

    boolean deleteMonthlyAgenda(Long monthlyAgendaId, SecurityUtils securityUtils);
}
