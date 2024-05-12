package com.example.rework.global.common;

import lombok.*;


@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Getter
@Setter
public class CommonResDto<T>{
    private int code; // 1(성공),-1(실패)
    private String message;
    private T data;


    @Builder
    public CommonResDto(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}