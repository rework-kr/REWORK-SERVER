package com.example.rework.mail.application.impl;

import com.example.rework.auth.MemberRole;
import com.example.rework.discord.WebhookService;
import com.example.rework.mail.MailPurpose;
import com.example.rework.mail.application.dto.MailRequestDto.SendMailDto;
import com.example.rework.mail.infra.MailSenderApi;
import com.example.rework.member.application.dto.MemberResponseDto.MemberCreateResponseDto;
import com.example.rework.member.application.impl.MemberServiceImpl;
import com.example.rework.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MailSenderServiceImplTest {

    @Mock
    private MemberServiceImpl memberService;
    @InjectMocks
    private MailSenderServiceImpl mailSenderService;
    @Mock
    private MailSenderApi mailSenderApi;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private WebhookService webhookService;

    @DisplayName("관리자는 일반유저를 회원가입 시킨 후 이메일을 전송할 수 있다.")
    @Test
    @WithMockUser(username = "testUserId", authorities = {"ADMIN"})
    void sendEmailAfterUserRegistration() throws Exception {
        // given
        SendMailDto sendMailDto = getSendMailDto();
        given(memberService.createMember(any())).willReturn(getMemberDto());
        given(memberRepository.findByUserId(any())).willReturn(Optional.empty());
        given(mailSenderApi.sendRandomPasswordMail(any(), any())).willReturn(true);
        given(webhookService.sendDiscordNotificationForRegister(any())).willReturn(true);
        // when
        boolean result = mailSenderService.sendMail(sendMailDto);
        //then
        assertTrue(result);
    }

    private SendMailDto getSendMailDto() {
        return SendMailDto.builder()
                .email("testUserId")
                .emailPurpose(MailPurpose.SIGNUP)
                .build();
    }

    private MemberCreateResponseDto getMemberDto() {
        return MemberCreateResponseDto.builder()
                .memberRole(MemberRole.MEMBER.name())
                .memberId(1L)
                .userId("testUserId")
                .build();
    }
}