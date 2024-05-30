package com.example.rework.monthlyagenda.application.dto;

import lombok.*;

public class MonthlyAgendaRequestDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReadMonthlyAgendaRequestDto{
        private int year;
        private int month;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class CreateMonthlyAgendaRequestDto{
        private String todo;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class UpdateMonthlyAgendaRequestDto{
        private Long agendaId;
        private String todo;
    }
}
