package com.example.rework.dailyagenda.application.dto;

import lombok.*;

import java.util.List;

public class DailyAgendaResponseDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class CreateDailyAgendaResponseDto {
        private Long agendaId;
        private String todo;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class UpdateDailyAgendaResponseDto {
        private Long agendaId;
        private String todo;
        private boolean state;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class ReadDailyAgendaResponseDto {
        private List<ReadDetailDailyAgendaResponseDto> agendaList;
        // 정렬관련 추가?
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReadDetailDailyAgendaResponseDto {
        private Long id;
        private String todo;
        private Long pagingId;
        private boolean state;

        public ReadDetailDailyAgendaResponseDto(Long id, String todo, boolean state,Long pagingId) {
            this.id = id;
            this.todo = todo;
            this.state = state;
            this.pagingId=pagingId;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReadDailyCompleteRateResponseDto {
        private int completeCount;
        private int allCount;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReadMonthlyCompleteRateResponseDto{
        private int completeCount;
        private int allCount;
    }
}
