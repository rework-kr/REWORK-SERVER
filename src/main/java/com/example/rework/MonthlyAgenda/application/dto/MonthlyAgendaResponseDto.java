package com.example.rework.MonthlyAgenda.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

public class MonthlyAgendaResponseDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class CreateMonthlyAgendaResponseDto{

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class UpdateMonthlyAgendaResponseDto{

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class ReadMonthlyAgendaResponseDto{
        private String todo;
        private boolean state;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm")
        private LocalDateTime createTime;
    }
}
