package com.example.rework.MonthlyAgenda.application;

import com.example.rework.MonthlyAgenda.application.dto.MonthlyAgendaRequestDto;
import com.example.rework.MonthlyAgenda.application.dto.MonthlyAgendaResponseDto;
import com.example.rework.config.security.SecurityUtils;

public interface MonthlyAgendaService {
    MonthlyAgendaResponseDto.ReadMonthlyAgendaResponseDto readMonthlyAgenda(MonthlyAgendaRequestDto.ReadMonthlyAgendaRequestDto readMonthlyAgendaRequestDto , SecurityUtils securityUtils);

    MonthlyAgendaResponseDto.CreateMonthlyAgendaResponseDto createMonthlyAgenda(MonthlyAgendaRequestDto.CreateMonthlyAgendaRequestDto createMonthlyAgendaRequestDto, SecurityUtils securityUtils);

    MonthlyAgendaResponseDto.UpdateMonthlyAgendaResponseDto updateMonthlyAgenda(MonthlyAgendaRequestDto.UpdateMonthlyAgendaRequestDto updateMonthlyAgendaRequestDto, SecurityUtils securityUtils);

    boolean deleteMonthlyAgenda(Long monthlyAgendaId, SecurityUtils securityUtils);
}
