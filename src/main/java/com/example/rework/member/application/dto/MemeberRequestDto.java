package com.example.rework.member.application.dto;

import com.example.rework.auth.MemberRole;
import com.example.rework.member.domain.Member;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

public class MemeberRequestDto {


    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MemberLoginRequestDto {

        @NotEmpty(message = "아이디 설정은 필수입니다.")
        @Size(min = 4, max = 32, message = "아이디를 4~32글자로 설정해주세요.")
        private String userId;

        @NotEmpty(message = "비밀번호 설정은 필수입니다.")
        @Size(min = 8, max = 64, message = "비밀번호를 8~64글자의 영문+숫자 조합으로 설정해주세요.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]+$", message = "비밀번호는 영문과 숫자의 조합이어야 합니다.")
        private String password;
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SignUpRequestDto {

        @NotEmpty(message = "이메일 인증은 필수입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "이메일 형식이 맞지 않습니다.")
        private String userId;

        @NotEmpty(message = "비밀번호 설정은 필수입니다.")
        @Size(min = 8, max = 64, message = "비밀번호를 8~64글자의 영문+숫자 조합으로 설정해주세요.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?!\\s+$).{8,64}$", message = "비밀번호는 영문, 숫자, 특수문자의 조합이어야 합니다.")
        private String password;

        @NotEmpty(message = "이름 설정은 필수입니다.")
        @Size(min = 1, max = 12, message = "이름을 12글자 이하로 설정헤주세요.")
        private String name;

        public Member toEntity() {
            return Member.builder()
                    .password(password)
                    .userId(userId)
                    .name(name)
                    .role(MemberRole.MEMBER)
                    .state(true)
                    .build();
        }

    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MemberRenewAccessTokenResponseDto {
        private String accessToken;
    }
    
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MemberUpdatePasswordRequestDto {

        @NotEmpty(message = "아이디 설정은 필수입니다.")
        @Size(min = 4, max = 32, message = "아이디를 4~32글자로 설정해주세요.")
        private String userId;

        @NotEmpty(message = "비밀번호 설정은 필수입니다.")
        @Size(min = 8, max = 64, message = "비밀번호를 8~64글자의 영문+숫자 조합으로 설정해주세요.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]+$", message = "비밀번호는 영문과 숫자의 조합이어야 합니다.")
        private String oldPassword;
        
        @NotEmpty(message = "비밀번호 설정은 필수입니다.")
        @Size(min = 8, max = 64, message = "비밀번호를 8~64글자의 영문+숫자 조합으로 설정해주세요.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]+$", message = "비밀번호는 영문과 숫자의 조합이어야 합니다.")
        private String newPassword;
    }

}
