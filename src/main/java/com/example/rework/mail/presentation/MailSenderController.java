package com.example.rework.mail.presentation;

import com.example.rework.global.common.CommonResDto;
import com.example.rework.mail.application.MailSenderService;
import com.example.rework.mail.application.dto.MailRequestDto;
import com.example.rework.mail.restapi.EmailApi;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequestMapping("/api/v1/admin/mails")
@RequiredArgsConstructor
public class MailSenderController implements EmailApi {

    private final MailSenderService mailSenderService;

    @Override
    @PostMapping("/send")
    public ResponseEntity<CommonResDto<?>> sendRandomPassword(MailRequestDto.SendMailDto sendMailDto) throws MessagingException {
        boolean result = mailSenderService.sendMail(sendMailDto);
        if(result){
            return new ResponseEntity<>(
                    new CommonResDto<>(1,"회원가입과 랜덤이메일전송이 정상적으로 이루어졌습니다.",""),HttpStatus.CREATED
            );
        }
        return new ResponseEntity<>(
                new CommonResDto<>(0,"회원가입에 실패하였습니다.",""),HttpStatus.BAD_REQUEST
        );

    }
}
