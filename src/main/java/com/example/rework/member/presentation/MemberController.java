package com.example.rework.member.presentation;

import com.example.rework.auth.cookie.CookieUtil;
import com.example.rework.auth.jwt.JwtProvider;
import com.example.rework.auth.service.RefreshTokenService;
import com.example.rework.global.common.CommonResDto;
import com.example.rework.global.error.InvalidTokenException;
import com.example.rework.member.application.MemberService;
import com.example.rework.member.application.dto.MemeberRequestDto;
import com.example.rework.member.restapi.MemberApi;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController implements MemberApi {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    //TODO 일단 회원가입은 관리자만할 수 있으니 TODO 처리
//    @Override
//    public ResponseEntity<CommonResDto<?>> memberSignUp(MemeberRequestDto.SignUpRequestDto signUpRequestDto) {
//        MemberResponseDto.MemberCreateResponseDto result = memberService.createMember(signUpRequestDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1,"회원 등록에 성공하였습니다.",result));
//    }

    @PostMapping("/renew-access-token")
    public ResponseEntity renewAccessToken(
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        try {
            String cookieRefreshToken = CookieUtil.getRefreshTokenCookie(request);
            Authentication authentication = jwtProvider.validateAndSetAuthentication(cookieRefreshToken);

            String accessToken = memberService.renewAccessToken(cookieRefreshToken, authentication);
            String refreshToken = jwtProvider.renewRefreshToken(authentication);

            refreshTokenService.setRefreshToken(authentication.getName(), refreshToken);
            CookieUtil.addCookie(response, "refreshToken", refreshToken, jwtProvider.REFRESH_TOKEN_EXPIRATION_TIME);

            return ResponseEntity.ok(MemeberRequestDto.MemberRenewAccessTokenResponseDto.builder()
                    .accessToken(accessToken)
                    .build());
        } catch (InvalidTokenException invalidTokenException) {
            throw new InvalidTokenException("토큰이 유효하지 않습니다");
        }
    }

    @Override
    @PostMapping("/logout")
    public ResponseEntity<CommonResDto<?>> logout(HttpServletResponse response, HttpServletRequest request) {
        String cookieRefreshToken = CookieUtil.getRefreshTokenCookie(request);
        log.info("logout controller : "+cookieRefreshToken);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            CookieUtil.deleteRefreshTokenCookie(request,response);
            System.out.println(auth.getName());
            memberService.memberLogout(auth.getName());
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return new ResponseEntity<>(
                new CommonResDto<>(1,"회원로그아웃성공",""),HttpStatus.OK
        );
    }


}
