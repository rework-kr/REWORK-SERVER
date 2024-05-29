package com.example.rework.member.application.impl;

import com.example.rework.auth.MemberRole;
import com.example.rework.auth.entity.RefreshToken;
import com.example.rework.auth.repository.RefreshTokenRepository;
import com.example.rework.member.application.dto.MemberResponseDto.MemberCreateResponseDto;
import com.example.rework.member.application.dto.MemeberRequestDto.SignUpRequestDto;
import com.example.rework.member.domain.Member;
import com.example.rework.member.domain.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @InjectMocks
    private MemberServiceImpl memberService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;



    @DisplayName("유저를 생성하게되면 memberId,name,userId,memberRole을 반환한다.")
    @Test
    void createMemberTest() throws IOException {
        //given
        SignUpRequestDto memberCreateReq = getMemberCreateReq();

        given(bCryptPasswordEncoder.encode(any(CharSequence.class)))
                .willReturn("encodedPassword");

        given(memberRepository.save(any(Member.class)))
                .willReturn(getMember());



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

    private RefreshToken getRefreshToken(){
        RefreshToken refreshToken = RefreshToken.builder()
                .token("refreshToken")
                .build();
        ReflectionTestUtils.setField(refreshToken, "id", "1");

        return refreshToken;
    }

    public Authentication getAuthentication() {
        List<String> roles = List.of("ROLE_MEMBER","ROLE_ADMIN");
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        User principal = new User("kbsserver@naver.com", "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

}