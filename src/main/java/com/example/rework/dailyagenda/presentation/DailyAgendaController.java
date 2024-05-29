package com.example.rework.dailyagenda.presentation;

import com.example.rework.config.security.SecurityUtils;
import com.example.rework.dailyagenda.application.DailyAgendaService;
import com.example.rework.dailyagenda.application.dto.DailyAgendaRequestDto.*;
import com.example.rework.dailyagenda.application.dto.DailyAgendaResponseDto.*;
import com.example.rework.dailyagenda.restapi.DailyAgendaApi;
import com.example.rework.global.common.CommonResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dailyAgenda")
@RequiredArgsConstructor
@Slf4j
public class DailyAgendaController implements DailyAgendaApi {
    private final DailyAgendaService dailyAgendaService;

    @Override
    public ResponseEntity<CommonResDto<?>> readDailyAgenda(ReadDailyAgendaRequestDto readDailyAgendaRequestDto, SecurityUtils securityUtils) {
        ReadDailyAgendaResponseDto result = dailyAgendaService.readDailyAgenda(readDailyAgendaRequestDto, securityUtils);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1, "오늘의 아젠다 조회에 성공", result));
    }

    @Override
    public ResponseEntity<CommonResDto<?>> crateDailyAgenda(CreateDailyAgendaRequestDto createDailyAgendaRequestDto, SecurityUtils securityUtils) {
        CreateDailyAgendaResponseDto result = dailyAgendaService.createDailyAgenda(createDailyAgendaRequestDto, securityUtils);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "오늘의 아젠다 생성 성공", result));
    }

    @Override
    public ResponseEntity<CommonResDto<?>> updateDailyAgenda(UpdateDailyAgendaRequestDto updateDailyAgendaRequestDto, SecurityUtils securityUtils) {
        UpdateDailyAgendaResponseDto result = dailyAgendaService.updateDailyAgenda(updateDailyAgendaRequestDto, securityUtils);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1, "오늘의 아젠다 수정 성공", result));
    }

    @Override
    public ResponseEntity<CommonResDto<?>> deleteDailyAgenda(Long dailyAgendaId, SecurityUtils securityUtils) {
        boolean result = dailyAgendaService.deleteDailyAgenda(dailyAgendaId, securityUtils);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1, "오늘의 아젠다 삭제 성공", result));
    }

    @Override
    public ResponseEntity<CommonResDto<?>> readDailyCompleteRate(ReadDailyCompleteRateRequestDto readDailyCompleteRateRequestDto, SecurityUtils securityUtils) {
        ReadDailyCompleteRateResponseDto result = dailyAgendaService.readDailyCompleteRate(readDailyCompleteRateRequestDto, securityUtils);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1, "오늘의 아젠다 수행률 조회 성공", result));
    }

    @Override
    public ResponseEntity<CommonResDto<?>> readMonthlyCompleteRate(ReadMonthlyCompleteRateRequestDto readMonthlyCompleteRateRequestDto, SecurityUtils securityUtils) {
        ReadMonthlyCompleteRateResponseDto result = dailyAgendaService.readMonthlyCompleteRate(readMonthlyCompleteRateRequestDto, securityUtils);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1, "이달의 누적 아젠다 수행률 조회 성공", result));
    }

    @Override
    public ResponseEntity<CommonResDto<?>> updateDailyAgendataPagingId (List<UpdateDailyAgendaListRequestDto> updateDailyAgendaListRequestDtoList , SecurityUtils securityUtils) {
        List<ReadDetailDailyAgendaResponseDto> result = dailyAgendaService.bulkUpdateDailyAgendaByPagingId(updateDailyAgendaListRequestDtoList, securityUtils);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1, "pagingId 별 오늘의 아젠다 업데이트 성공", result));
    }

}
