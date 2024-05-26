package com.example.rework.monthlyagenda.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

public class MonthlyAgendaResponseDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class CreateMonthlyAgendaResponseDto {
        private Long agendaId;
        private String todo;
        private boolean state;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class UpdateMonthlyAgendaResponseDto {
        private Long agendaId;
        private String todo;
        private boolean state;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class ReadMonthlyAgendaResponseDto {
        private Long agendaId;
        private String todo;
        private boolean state;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
        private LocalDateTime createTime;
    }
}
