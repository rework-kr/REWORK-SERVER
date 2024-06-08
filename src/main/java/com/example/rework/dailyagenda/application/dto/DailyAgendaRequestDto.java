package com.example.rework.dailyagenda.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class DailyAgendaRequestDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateDailyAgendaRequestDto {
        private String todo;
        private Long pagingId;
        private LocalDate createdAt;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateDailyAgendaListRequestDto {
        private Long agendaId;
        private String todo;
        private boolean state;
        private Long pagingId;
        private LocalDate createdAt;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateDailyAgendaRequestDto {
        private Long agendaId;
        private String todo;
        private boolean state;
        private Long pagingId;
    }

}
