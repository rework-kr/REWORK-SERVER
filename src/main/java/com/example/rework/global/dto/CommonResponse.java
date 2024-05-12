package com.example.rework.global.dto;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CommonResponse {
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    @Builder.Default
    private Date dateTime = new Date();
    private boolean success;
    private Object response;
    private Object error;

}