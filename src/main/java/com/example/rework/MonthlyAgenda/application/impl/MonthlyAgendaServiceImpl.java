package com.example.rework.MonthlyAgenda.application.impl;

import com.example.rework.MonthlyAgenda.application.MonthlyAgendaService;
import com.example.rework.MonthlyAgenda.application.dto.MonthlyAgendaRequestDto;
import com.example.rework.MonthlyAgenda.application.dto.MonthlyAgendaResponseDto;
import com.example.rework.MonthlyAgenda.domain.MonthlyAgenda;
import com.example.rework.MonthlyAgenda.domain.repository.MonthlyAgendaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MonthlyAgendaServiceImpl implements MonthlyAgendaService {
    private final MonthlyAgendaRepository monthlyAgendaRepository;
    @Override
    public MonthlyAgendaResponseDto.ReadMonthlyAgendaResponseDto readMonthlyAgenda(String date, Long currentUserId) {
        return null;
    }

    @Override
    public MonthlyAgendaResponseDto.CreateMonthlyAgendaResponseDto createMonthlyAgenda(MonthlyAgendaRequestDto.CreateMonthlyAgendaRequestDto createMonthlyAgendaRequestDto, Long currentUserId) {
        return null;

    }

    @Override
    public MonthlyAgendaResponseDto.UpdateMonthlyAgendaResponseDto updateMonthlyAgenda(MonthlyAgendaRequestDto.UpdateMonthlyAgendaRequestDto updateMonthlyAgendaRequestDto, Long currentUserId) {
        return null;
    }

    @Override
    public boolean deleteMonthlyAgenda(Long monthlyAgendaId, Long currentUserId) {
        return false;
    }
}
