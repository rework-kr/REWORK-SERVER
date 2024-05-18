package com.example.rework.member.restapi;

import com.example.rework.global.common.CommonResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원 API", description = "회원 관련 API")
@RestController
@Validated
@RequestMapping("/api/v1/members")
public interface MemberApi {


    //TODO 일단 회원가입은 관리자만할 수 있으니 TODO 처리
//    @Operation(
//            summary = "회원 생성",
//            description =
//                    "회원 정보를 받아 회원가입을 하는 API\n"
//                            + "회원가입 성공 시 201 Created 반환"
//    )
//    @PostMapping("/signup")
//    ResponseEntity<CommonResDto<?>> memberSignUp(
//            @Valid
//            @RequestBody
//            SignUpRequestDto signUpRequestDto
//    );

    @Operation(
            summary = "회원 accessToken 재발급 요청",
            description =
                    "refreshToken값으로 accessToken을 재발급하는 API"
    )
    @PostMapping("/renew-access-token")
    ResponseEntity<CommonResDto<?>> renewAccessToken(
            HttpServletResponse response,
            HttpServletRequest request
    );

    @Operation(
            summary = "회원로그아웃을 위해 쿠키에 담겨있는 refreshToken을 삭제하는 API",
            description =
                    "회원로그아웃을 위해 쿠키에 담겨있는 refreshToken을 삭제하는 API"
    )
    @PostMapping("/logout")
    ResponseEntity<CommonResDto<?>> logout(
            HttpServletResponse response,
            HttpServletRequest request
    );
}
