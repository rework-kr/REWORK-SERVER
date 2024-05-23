package com.example.rework.MonthlyAgenda.presentation;

import com.example.rework.MonthlyAgenda.application.MonthlyAgendaService;
import com.example.rework.MonthlyAgenda.application.dto.MonthlyAgendaRequestDto.*;
import com.example.rework.MonthlyAgenda.application.dto.MonthlyAgendaResponseDto;
import com.example.rework.MonthlyAgenda.restapi.MonthlyAgendaApi;
import com.example.rework.config.security.SecurityUtils;
import com.example.rework.global.common.CommonResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/monthlyAgenda")
@RequiredArgsConstructor
@Slf4j
public class MonthlyAgendaController implements MonthlyAgendaApi {
    private final MonthlyAgendaService monthlyAgendaService;

    @Override
    public ResponseEntity<CommonResDto<?>> readMonthlyAgenda(ReadMonthlyAgendaRequestDto readMonthlyAgendaRequestDto, SecurityUtils securityUtils) {
        MonthlyAgendaResponseDto.ReadMonthlyAgendaResponseDto result = monthlyAgendaService.readMonthlyAgenda(readMonthlyAgendaRequestDto, securityUtils);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1, "이번달 아젠다 조회에 성고했습니다.", result));
    }

    @Override
    public ResponseEntity<CommonResDto<?>> createMonthlyAgenda(CreateMonthlyAgendaRequestDto createMonthlyAgendaRequestDto, SecurityUtils securityUtils) {
        MonthlyAgendaResponseDto.CreateMonthlyAgendaResponseDto result = monthlyAgendaService.createMonthlyAgenda(createMonthlyAgendaRequestDto, securityUtils);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1, "이번달 아젠다 생성에 성공했습니다.", result));
    }

    @Override
    public ResponseEntity<CommonResDto<?>> updateMonthlyAgenda(UpdateMonthlyAgendaRequestDto updateMonthlyAgendaRequestDto, SecurityUtils securityUtils) {
        MonthlyAgendaResponseDto.UpdateMonthlyAgendaResponseDto result = monthlyAgendaService.updateMonthlyAgenda(updateMonthlyAgendaRequestDto, securityUtils);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1, "이번달 아젠달 수정에 성공했습니다.", result));
    }

    @Override
    public ResponseEntity<CommonResDto<?>> deleteMonthlyAgenda(Long monthlyAgendaId, SecurityUtils securityUtils) {
        boolean result = monthlyAgendaService.deleteMonthlyAgenda(monthlyAgendaId, securityUtils);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1, "이번달 아젠다 삭제에 성공했습니다.", result));
    }

}
