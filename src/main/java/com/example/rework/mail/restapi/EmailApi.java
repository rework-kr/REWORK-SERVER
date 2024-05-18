package com.example.rework.mail.restapi;

import com.example.rework.global.common.CommonResDto;
import com.example.rework.mail.application.dto.MailRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "이메일 API", description = "이메일 관련 API")
@RestController
@Validated
@RequestMapping("/api/v1/mails")
public interface EmailApi {

    @Operation(
            summary = "이메일 생성",
            description =
                    "랜덤난수 패스워드로 회원가입을 한 후 해당 이메일로 비밀번호를 보내주는 API"+
                            "admin만 사용 가능한 API"
    )
    @PostMapping("/send")
    ResponseEntity<CommonResDto<?>> sendRandomPassword(
            @Validated @RequestBody MailRequestDto.SendMailDto sendMailDto
    ) throws MessagingException;
}
