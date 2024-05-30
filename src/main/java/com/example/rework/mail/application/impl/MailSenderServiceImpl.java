package com.example.rework.mail.application.impl;

import com.example.rework.discord.WebhookService;
import com.example.rework.global.error.DuplicateAccountException;
import com.example.rework.global.error.NotFoundAccountException;
import com.example.rework.mail.MailPurpose;
import com.example.rework.mail.application.MailSenderService;
import com.example.rework.mail.application.dto.MailRequestDto.SendMailDto;
import com.example.rework.mail.infra.MailSenderApi;
import com.example.rework.mail.utils.GeneRateRandomPassword;
import com.example.rework.member.application.MemberService;
import com.example.rework.member.application.dto.MemberResponseDto.MemberCreateResponseDto;
import com.example.rework.member.application.dto.MemeberRequestDto.SignUpRequestDto;
import com.example.rework.member.domain.Member;
import com.example.rework.member.domain.repository.MemberRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MailSenderServiceImpl implements MailSenderService {


    private final MemberRepository memberRepository;
    private final MailSenderApi mailSenderApi;
    private final MemberService memberService;
    private final WebhookService webhookService;



    @Override
    @Transactional
    public boolean sendMail(SendMailDto sendMailDto) throws MessagingException {
        final String email = sendMailDto.getEmail();
        MailPurpose emailPurpose = sendMailDto.getEmailPurpose();
        final String randomPassword = GeneRateRandomPassword.getRandomPassword();
        //유효성 체크
        emailValidationCheck(email, emailPurpose.name());
        //회원가입 DTO 생성
        SignUpRequestDto signUpRequestDto = createMemberDto(email, randomPassword, "민돌이");
        //회원가입
        MemberCreateResponseDto member = memberService.createMember(signUpRequestDto);
        //회원가입 한 이메일로 랜덤 비밀번호 전송
        if(mailSenderApi.sendRandomPasswordMail(sendMailDto,randomPassword)){
            webhookService.sendDiscordNotification(email);
            return true;
        }
        return false;
    }

    private SignUpRequestDto createMemberDto(String email,String password, String name) {
        return SignUpRequestDto.builder()
                .userId(email)
                .password(password)
                .name(name)
                .build();
    }

    private void emailValidationCheck(String email, String verifyPurpose) {
        if(verifyPurpose.equals("FORGOT_PASSWORD")){
            memberRepository.findByUserId(email).orElseThrow(NotFoundAccountException::new);
        }
        if(verifyPurpose.equals("SIGNUP")){
            Optional<Member> member = memberRepository.findByUserId(email);
            if(member.isPresent()){
                throw new DuplicateAccountException("중복된 회원입니다");
            }
        }
    }
}
