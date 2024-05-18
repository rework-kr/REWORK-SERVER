package com.example.rework.mail.application.dto;

import com.example.rework.mail.MailPurpose;
import lombok.*;

public class MailRequestDto {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SendMailDto {
        //보내려는 사람의 email
        String email;
        MailPurpose emailPurpose;
    }


    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class VerificationMailDto {
        //보내려는 사람의 email
        String email;
        String randomNumber;
        String emailPurpose;

    }

}
