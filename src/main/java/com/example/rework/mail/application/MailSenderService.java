package com.example.rework.mail.application;


import com.example.rework.mail.application.dto.MailRequestDto;
import jakarta.mail.MessagingException;

public interface MailSenderService {
    //인증 번호 전송 메소드
    boolean sendMail(MailRequestDto.SendMailDto sendMailDto) throws MessagingException;

}
