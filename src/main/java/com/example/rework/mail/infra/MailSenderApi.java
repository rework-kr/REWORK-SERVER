package com.example.rework.mail.infra;

import com.example.rework.mail.application.dto.MailRequestDto.SendMailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class MailSenderApi {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public boolean sendRandomPasswordMail(SendMailDto sendMailDto,String randomPassword) throws MessagingException, MessagingException {
        String email=sendMailDto.getEmail();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("플로우 빗 인증메일");

        //템플릿에 전달할 데이터 설정
        Context context = new Context();
        context.setVariable("randomNumber", randomPassword);

        //메일 내용 설정 : 템플릿 프로세스
        String html = templateEngine.process("acceptEmail.html",context);
        helper.setText(html, true);
        //메일 보내기
        javaMailSender.send(message);
        return true;
    }
}
