package com.example.rework.member.restapi;

import com.example.rework.config.security.SecurityUtils;
import com.example.rework.global.common.CommonResDto;
import com.example.rework.member.application.dto.MemeberRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
//            MemeberRequestDto.SignUpRequestDto signUpRequestDto
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

    @Operation(
            summary = "패스워드 변경을 위한 API 입니다.",
            description =
                    "패스워드 변경을 위해서 Authorization 헤더에 accessToken을 넣어서 요청합니다."
    )
    @PutMapping("/password")
    ResponseEntity<CommonResDto<?>> updatePassword(
            @RequestBody MemeberRequestDto.MemberUpdatePasswordRequestDto updatePasswordRequestDto,
            SecurityUtils securityUtils
            );


    @Operation(
            summary = "초기 회원가입을 위한 이메일 입력하는 API입니다.",
            description =
                    "어떤 유저든 이메일을 등록하여 ADMIN에게 회원가입 요청을 할 수 있습니다."
    )
    @PostMapping("/register-email")
    ResponseEntity<CommonResDto<?>> nonMemberRegisterEmail(
            @RequestBody MemeberRequestDto.RegisterEmailRequestDto registerEmailRequestDto
    );

    @Operation(
            summary = "admin이 이메일 리스트를 확인하는 API입니다.",
            description =
                    "admin이 이메일 리스트를 확인하는 API입니다."
    )
    @GetMapping("/admin/register-email")
    ResponseEntity<CommonResDto<?>> adminNonMemberEamilList(
            SecurityUtils securityUtils
    );

    @Operation(
            summary = "유저의 정보를 조회하는 API 입니다.",
            description =
                    "Authorization header bearer token을 통해 유저의 정보를 조회하는 API 입니다."
    )
    @GetMapping("/info")
    ResponseEntity<CommonResDto<?>> readMemberInfo(
            SecurityUtils securityUtils
    );
}
