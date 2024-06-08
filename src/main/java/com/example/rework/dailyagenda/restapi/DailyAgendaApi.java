package com.example.rework.dailyagenda.restapi;

import com.example.rework.config.security.SecurityUtils;
import com.example.rework.dailyagenda.application.dto.DailyAgendaRequestDto.*;
import com.example.rework.global.common.CommonResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "오늘의 아젠다 API", description = "오늘의 아젠다 관련 API")
@RestController
@RequestMapping("/api/v1/dailyAgenda")
@Validated
public interface DailyAgendaApi {
    @Operation(
            summary = "이번달 아젠다 조회",
            description = "사용자 정보를 받아 '오늘의 아젠다를' 조회하는 API \n"
                    + "조회시 옵션 3가지 제공. 공백, true, false에 따라 해당하는 아젠다를 조회."
    )
    @GetMapping
    ResponseEntity<CommonResDto<?>> readDailyAgenda(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @RequestParam("day") int day,
            @RequestParam("state") Boolean state,
            SecurityUtils securityUtils
    );

    @Operation(
            summary = "이번달 아젠다 생성",
            description = "아젠다를 입력받아 '오늘의 아젠다'를 생성함"
    )
    @PostMapping
    ResponseEntity<CommonResDto<?>> crateDailyAgenda(
            @RequestBody CreateDailyAgendaRequestDto createDailyAgendaRequestDto,
            SecurityUtils securityUtils
    );

    @Operation(
            summary = "이번달 아젠다 수정",
            description = "수정된 아젠다를 입력받아 '오늘의 아젠다'를 수정함"
    )
    @PutMapping
    ResponseEntity<CommonResDto<?>> updateDailyAgenda(
            @RequestBody UpdateDailyAgendaRequestDto updateDailyAgendaRequestDto,
            SecurityUtils securityUtils
    );

    @Operation(
            summary = "이번달 아젠다 삭제",
            description = "삭제하고자 하는 아젠다id를 입력 받아 해당하는 '오늘의 아젠다'를 삭제함"
    )
    @DeleteMapping
    ResponseEntity<CommonResDto<?>> deleteDailyAgenda(
            @RequestParam("dailyAgendaId") Long dailyAgendaId,
            SecurityUtils securityUtils
    );

    // 아젠다 성공 상태변화ㅏ

    // 완료되지 못한 오늘의 아젠다는 다음날로 복사한다.
    // 하지만 전의 날짜를 클릭해도 해당하는 날짜의 미완성 아젠다를 조회할수는있다.

    //오늘의 아젠다 수행률 (하루기준
    @Operation(
            summary = "오늘의 아젠다 수행률 조회 (하루기준)",
            description = "오늘의 아젠다의 수행률을 조회한다. 사용자 정보와 날짜 정보를 받는다.\n"
                    + " 아젠다의 총 개수와 성공한 개수를 반환한다."
    )
    @GetMapping("/dailyCompleteRate")
    ResponseEntity<CommonResDto<?>> readDailyCompleteRate(
            @RequestBody ReadDailyCompleteRateRequestDto readDailyCompleteRateRequestDto,
            SecurityUtils securityUtils
    );

    @Operation(
            summary = "오늘의 아젠다 수행률 조회 (한달기준)",
            description = "오늘의 아젠다의 수행률을 조회한다. 사용자 정보와 날짜 정보를 받는다. \n"
                    + "아젠다의 총 개수와 성공한 개수를 반환한다."
    )
    @GetMapping("/monthlyCompleteRate")
    ResponseEntity<CommonResDto<?>> readMonthlyCompleteRate(
            @RequestBody ReadMonthlyCompleteRateRequestDto readMonthlyCompleteRateRequestDto,
            SecurityUtils securityUtils
    );

    //아젠다 정렬
    @Operation(
            summary = "오늘의 아젠다 pagingId 별 정렬",
            description = "오늘의 아젠다의 페이지의 세션이 종료될 때 pagingId 값을 전부 넘긴다. 해당 pagingId로 서버에서 벌크업데이트를 시킨다 \n"
                    + "프론트에서 정렬해서 준 agenda를 DB에저장하고 반환한다. \n"+
                    "{\n" +
                    "    \"todo\":\"오늘의 아젠다5\",\n" +
                    "    \"pagingId\":3,\n" +
                    "    \"createdAt\":\"2024-05-29\"\n" +
                    "}"
    )
    @PutMapping("/bulk-update-pagingId")
    ResponseEntity<CommonResDto<?>> updateDailyAgendataPagingId(
            @RequestBody List<UpdateDailyAgendaListRequestDto> updateDailyAgendaListRequestDtoList,
            SecurityUtils securityUtils
    );


}
