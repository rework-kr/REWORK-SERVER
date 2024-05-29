package com.example.rework.dailyagenda.application;

import com.example.rework.config.security.SecurityUtils;
import com.example.rework.dailyagenda.application.dto.DailyAgendaRequestDto.*;
import com.example.rework.dailyagenda.application.dto.DailyAgendaResponseDto.*;

import java.util.List;

public interface DailyAgendaService {
    ReadDailyAgendaResponseDto readDailyAgenda(ReadDailyAgendaRequestDto readDailyAgendaRequestDto, SecurityUtils securityUtils);

    CreateDailyAgendaResponseDto createDailyAgenda(CreateDailyAgendaRequestDto createDailyAgendaRequestDto, SecurityUtils securityUtils);

    UpdateDailyAgendaResponseDto updateDailyAgenda(UpdateDailyAgendaRequestDto updateDailyAgendaRequestDto, SecurityUtils securityUtils);

    boolean deleteDailyAgenda(Long dailyAgendaId, SecurityUtils securityUtils);

    ReadDailyCompleteRateResponseDto readDailyCompleteRate(ReadDailyCompleteRateRequestDto readDailyCompleteRateRequestDto, SecurityUtils securityUtils);

    ReadMonthlyCompleteRateResponseDto readMonthlyCompleteRate(ReadMonthlyCompleteRateRequestDto readMonthlyCompleteRateRequestDto, SecurityUtils securityUtils);

    List<ReadDetailDailyAgendaResponseDto> bulkUpdateDailyAgendaByPagingId(List<UpdateDailyAgendaListRequestDto> updateDailyAgendaListRequestDtoList, SecurityUtils securityUtils);

}