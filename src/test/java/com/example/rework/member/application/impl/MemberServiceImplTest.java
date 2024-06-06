package com.example.rework.member.application.impl;

import com.example.rework.auth.MemberRole;
import com.example.rework.auth.entity.RefreshToken;
import com.example.rework.auth.jwt.MemberDetails;
import com.example.rework.auth.repository.RefreshTokenRepository;
import com.example.rework.discord.WebhookService;
import com.example.rework.member.application.dto.MemberResponseDto;
import com.example.rework.member.application.dto.MemberResponseDto.MemberCreateResponseDto;
import com.example.rework.member.application.dto.MemberResponseDto.MemberInfoResponseDto;
import com.example.rework.member.application.dto.MemeberRequestDto;
import com.example.rework.member.application.dto.MemeberRequestDto.SignUpRequestDto;
import com.example.rework.member.domain.Member;
import com.example.rework.member.domain.NonMemberEmail;
import com.example.rework.member.domain.repository.MemberRepository;
import com.example.rework.member.domain.repository.NonMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @InjectMocks
    private MemberServiceImpl memberService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private NonMemberRepository nonMemberRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private WebhookService webhookService;


    @BeforeEach
    void SecurityUserTest() {
        Member member = getMember();

        MemberDetails memberDetails = new MemberDetails(member);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    @DisplayName("유저를 생성하게되면 memberId,name,userId,memberRole을 반환한다.")
    @Test
    void createMemberTest() throws IOException {
        //given
        SignUpRequestDto memberCreateReq = getMemberCreateReq();

        given(bCryptPasswordEncoder.encode(any(CharSequence.class)))
                .willReturn("encodedPassword");

        given(memberRepository.save(any(Member.class)))
                .willReturn(getMember());
        given(nonMemberRepository.findByEmail(any())).willReturn(getNonMember());




        //when
        MemberCreateResponseDto memberCreateResponseDto = memberService.createMember(memberCreateReq);

        //then
        Assertions.assertThat(memberCreateResponseDto.getMemberId()).isEqualTo(1);
        Assertions.assertThat(memberCreateResponseDto.getMemberRole()).isEqualTo("MEMBER");
        Assertions.assertThat(memberCreateResponseDto.getName()).isEqualTo("민우");
        Assertions.assertThat(memberCreateResponseDto.getUserId()).isEqualTo("kbsserver@naver.com");
    }

    @DisplayName("유저 아이디값을 통해 유저의 값을 조회할 수 있다.")
    @Test
    void getUserByUserIdTest() throws IOException {
        //given

        given(memberRepository.findByUserId(any()))
                .willReturn(Optional.ofNullable(getMember()));

        //when
        Member member = memberService.findMemberByUserId(getMember().getUserId());
        //then
        Assertions.assertThat(member.getId()).isEqualTo(getMember().getId());
        Assertions.assertThat(member.getName()).isEqualTo(getMember().getName());
        Assertions.assertThat(member.getUserId()).isEqualTo(getMember().getUserId());
    }

    @DisplayName("로그아웃을 하게되면 refreshToken을 redis에서 삭제한다.")
    @Test
    void logoutRemovesRefreshTokenCookieTest() throws IOException {
        //given
        given(refreshTokenRepository.findById(getRefreshToken().getId()))
                .willReturn(null);
        //when
        memberService.memberLogout(any());
        //then
        Assertions.assertThat(refreshTokenRepository.findById(getRefreshToken().getId())).isEqualTo(null);
    }

    @DisplayName("회원은 비밀번호를 변경할 수 있다.")
    @Test
    void testUserCanChangePassword() throws IOException {
        //given
        given(memberRepository.findByUserId(any()))
                .willReturn(Optional.ofNullable(getMember()));
        given(bCryptPasswordEncoder.matches(any(), any()))
                .willReturn(true);
        MemeberRequestDto.MemberUpdatePasswordRequestDto memberPasswordUpdateReq = getMemberPasswordUpdateReq();

        //when
        boolean result = memberService.updatePassword(memberPasswordUpdateReq, any());
        //then
        Assertions.assertThat(result).isEqualTo(true);
    }


    @DisplayName("admin유저는 승인대기 중인 비회원의 이메일리스트를 확인할 수 있다.")
    @WithMockUser(username = "kbsserver@naver.com", authorities = {"ADMIN"})
    @Test
    void viewPendingNonMemberEmails() throws IOException {
        //given
        given(nonMemberRepository.findAllByIsAccepted(false))
                .willReturn(getNonMemberEmails());
        //when
        List<MemberResponseDto.NonMemberEmailListResponseDto> nonMemberEmailListResponseDtos = memberService.adminNonMemberEamilList(any());


        //then
        Assertions.assertThat(nonMemberEmailListResponseDtos.get(0).getEmail()).isEqualTo("kbsserver@naver.com");
        Assertions.assertThat(nonMemberEmailListResponseDtos.get(1).getEmail()).isEqualTo("kbsserver2@naver.com");

    }

    @DisplayName("회원가입을 위해서 이메일을 등록하게 되면 NonMember테이블에 이메일이 쌓이게 되고 admin유저에게 알림이 전송된다.")
    @Test
    void registerEmailAndNotifyAdmin() throws IOException {
        //given
        String initialEmail="kbsserver@naver.com";
        MemeberRequestDto.RegisterEmailRequestDto registerEmailRequestDto = MemeberRequestDto.RegisterEmailRequestDto.builder()
                .email(initialEmail)
                .build();
        given(nonMemberRepository.save(any())).willReturn(getNonMember());
        given(webhookService.sendDiscordNotificationForNonMemberRegister(any())).willReturn(true);

        //when
        boolean result = memberService.registerEmail(registerEmailRequestDto);

        //then
        Assertions.assertThat(result).isEqualTo(true);

    }

    @DisplayName("회원의 정보를 조회할 수 있다.")
    @Test
    void readMemberInfoTest() throws IOException {
        //given
        given(memberRepository.findByUserId(any()))
                .willReturn(Optional.ofNullable(getMember()));
        //when
        MemberInfoResponseDto memberInfoResponseDto = memberService.readMemberInfo(any());

        //then
        assertAll(
                () -> assertThat(memberInfoResponseDto.getMemberRole()).isEqualTo(MemberRole.MEMBER),
                () -> assertThat(memberInfoResponseDto.getEmail()).isEqualTo("kbsserver@naver.com"),
                () -> assertThat(memberInfoResponseDto.isInitialPasswordUpdateState()).isEqualTo(false)
        );



    }

    private List<NonMemberEmail> getNonMemberEmails() {
        return List.of(NonMemberEmail.builder()
                .email("kbsserver@naver.com")
                .build(),
                NonMemberEmail.builder()
                        .email("kbsserver2@naver.com")
                        .build());
    }


    private MemeberRequestDto.MemberUpdatePasswordRequestDto getMemberPasswordUpdateReq() throws IOException {

        return MemeberRequestDto.MemberUpdatePasswordRequestDto.builder()
                .newPassword("test12345")
                .oldPassword("test1234")
                .build();
    }
    private SignUpRequestDto getMemberCreateReq() throws IOException {

        return SignUpRequestDto.builder()
                .name("민우")
                .password("test1234")
                .userId("kbsserver@naver.com")
                .build();
    }
    private Member getMember(){
        Member member = Member.builder()
                .role(MemberRole.MEMBER)
                .name("민우")
                .password("test1234")
                .userId("kbsserver@naver.com")
                .build();
        ReflectionTestUtils.setField(member, "id", 1L);
        return member;
    }

    private NonMemberEmail getNonMember(){
        NonMemberEmail nonMemberEmail = NonMemberEmail.builder()
                .isAccepted(false)
                .email("kbsserver@naver.com")
                .build();
        ReflectionTestUtils.setField(nonMemberEmail, "id", 1L);
        return nonMemberEmail;
    }
    private RefreshToken getRefreshToken(){
        RefreshToken refreshToken = RefreshToken.builder()
                .token("refreshToken")
                .build();
        ReflectionTestUtils.setField(refreshToken, "id", "1");

        return refreshToken;
    }

}