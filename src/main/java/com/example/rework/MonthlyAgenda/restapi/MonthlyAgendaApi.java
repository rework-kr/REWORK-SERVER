package com.example.rework.MonthlyAgenda.restapi;

import com.example.rework.config.security.SecurityUtils;
import com.example.rework.global.common.CommonResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import com.example.rework.MonthlyAgenda.application.dto.MonthlyAgendaRequestDto.*;

@Tag(name = "이번달 아젠다 API", description = "이번달 아젠다 관련 API")
@RestController
@RequestMapping("/api/v1/monthlyAgenda")
@Validated
public interface MonthlyAgendaApi {

    @Operation(
            summary = "이번달 아젠다 조회",
            description = "사용자 정보와 연,월을 받아 해당하는 달의 '이번달 아젠다를' 조회하는 API"
    )
    @GetMapping("/read")
    ResponseEntity<CommonResDto<?>> readMonthlyAgenda(
            @RequestBody ReadMonthlyAgendaRequestDto readMonthlyAgendaRequestDto,
            SecurityUtils securityUtils
    );

    @Operation(
            summary = "이번달 아젠다 등록",
            description = "사용자가 입력한 이번달 아젠다를 저장하는 API"
    )
    @PostMapping("/create")
    ResponseEntity<CommonResDto<?>> createMonthlyAgenda(
            @Valid
            @RequestBody
            CreateMonthlyAgendaRequestDto createMonthlyAgendaRequestDto,
            SecurityUtils securityUtils
    );

    @Operation(
            summary = "이번달 아젠다 수정",
            description = "사용자가 설정한 이번달 아젠다를 수정하는 API"
    )
    @PutMapping("/update")
    ResponseEntity<CommonResDto<?>> updateMonthlyAgenda(
            @RequestBody
            UpdateMonthlyAgendaRequestDto updateMonthlyAgendaRequestDto,
            SecurityUtils securityUtils
    );


    @Operation(
            summary = "이번달 아젠다 삭제",
            description = "이번달 아젠다를 삭제하는 API, 현재는 필요하지 않음."
    )
    @DeleteMapping("/delete")
    ResponseEntity<CommonResDto<?>> deleteMonthlyAgenda(
            @RequestParam("monthlyAgendaId") Long monthlyAgendaId,
            SecurityUtils securityUtils
    );
}
