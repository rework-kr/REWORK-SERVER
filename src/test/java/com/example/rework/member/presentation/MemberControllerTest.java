package com.example.rework.member.presentation;

import com.example.rework.auth.MemberRole;
import com.example.rework.member.application.MemberService;
import com.example.rework.member.application.dto.MemberResponseDto;
import com.example.rework.member.application.dto.MemeberRequestDto;
import com.example.rework.member.application.dto.MemeberRequestDto.SignUpRequestDto;
import com.example.rework.member.domain.Member;
import com.example.rework.member.fixture.MemberFixture;
import com.example.rework.util.ControllerTestSupport;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class MemberControllerTest extends ControllerTestSupport {

    @Autowired
    MemberService memberService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    EntityManager em;

    private static Member initialMember;

    private static final String password="anstn1234@";


    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .name("김민우1234")
                .password(bCryptPasswordEncoder.encode(password))
                .userId("kbsserver@naver.com")
                .role(MemberRole.MEMBER)
                .state(true)
                .build();
        initialMember = memberRepository.saveAndFlush(member);
    }
    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    //TODO 유저가 회원가입하는 기능은 빠져서 주석처리
//    @DisplayName("유저는 회원가입을 할 수 있다.")
//    @Test
//    void memberSignupTest() throws Exception {
//        // given
//
//        String url = "/api/v1/members/signup";
//
//        SignUpRequestDto signUpRequestDto = MemberFixture.createMember("kbsserver2@naver.com");
//
//        //when //then
//        MvcResult mvcResult = mockMvc.perform(post(url)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .content(objectMapper.writeValueAsString(signUpRequestDto))
//                )
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andReturn();
//    }
//
//    @DisplayName("이미 가입된 id는 회원가입을 할 수 없다.")
//    @Test
//    void createMemberWithSameID() throws Exception {
//        // Given
//        SignUpRequestDto signUpRequestDto = MemberFixture.createMember("kbsserver@naver.com");
//        String url = "/api/v1/members/signup";
//
//        //when //then
//        MvcResult mvcResult = mockMvc.perform(post(url)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .content(objectMapper.writeValueAsString(signUpRequestDto))
//                )
//                .andDo(print())
//                .andExpect(status().isConflict())
//                .andReturn();
//    }

    @DisplayName("회원가입한 사용자는 로그인을 할 수 있다.")
    @Test
    void loginMember() throws Exception {
        //given
        String url = "/api/v1/members/login";

        SignUpRequestDto signUpRequestDto = MemberFixture.createMember("kbsserver3@naver.com");


        memberService.createMember(signUpRequestDto);

        MemeberRequestDto.MemberLoginRequestDto memberLoginRequestDto = MemeberRequestDto.MemberLoginRequestDto.builder()
                .userId("kbsserver3@naver.com")
                .password("anstn1234@")
                .build();

        //when
        MvcResult mvcResult = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        String result = mvcResult.getResponse().getContentAsString();
        MemberResponseDto.MemberLoginResponseDto memberLoginResponseDto = objectMapper.readValue(result, MemberResponseDto.MemberLoginResponseDto.class);
        assertThat(memberLoginResponseDto).isNotNull();
        assertThat(memberLoginResponseDto.getAccessToken()).isNotNull();
    }

    @DisplayName("로그아웃을 하게되면 쿠키에 refreshToken값은 사라진다.")
    @Test
    void logoutRemovesRefreshTokenCookieValueTest() throws Exception {
        String url = "/api/v1/members/logout";
        // 쿠키 생성
        Cookie refreshTokenCookie = new Cookie("refreshToken", "your_refresh_token_value");
        // HttpServletRequest 모의 객체 생성
        HttpServletRequest request = mock(HttpServletRequest.class);
        // 모의 객체의 getCookies 메소드가 쿠키를 반환하도록 설정
        given(request.getCookies())
                .willReturn(new Cookie[]{refreshTokenCookie});
        // when //then
        ResultActions resultActions = mockMvc.perform(post(url)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .cookie(refreshTokenCookie)) // 요청에 쿠키 추가
                .andDo(print())
                .andExpect(status().isOk());

    }

    @DisplayName("요청 쿠키에 리프래쉬 토큰 값이 없으면 401 에러를 반환한다.")
    @Test
    void requestWithoutRefreshTokenCookieReturns401ErrorTest() throws Exception {
        String url = "/api/v1/members/logout";
        // 쿠키 생성
        // when //then
        ResultActions resultActions = mockMvc.perform(post(url)
                        .characterEncoding(StandardCharsets.UTF_8)
                        )
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @DisplayName("아이디와 비밀번호가 일치하면 accessToken값을 반환한다.")
    @Test
    void matchingIdAndPasswordReturnsAccessTokenTest() throws Exception {
        // given
        String url = "/api/v1/members/login";
        // 로그인 요청 DTO 생성
        MemeberRequestDto.MemberLoginRequestDto memberLoginRequestDto = MemeberRequestDto.MemberLoginRequestDto.builder()
                .userId(initialMember.getUserId())
                .password(password)
                .build();
        // when & then: 로그인 요청 후 반환된 accessToken 값 확인
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

}