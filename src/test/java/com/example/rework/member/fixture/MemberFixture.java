package com.example.rework.member.fixture;

import com.example.rework.member.application.dto.MemeberRequestDto;

public class MemberFixture {

    public static MemeberRequestDto.SignUpRequestDto createMember(String userId){
        return  MemeberRequestDto.SignUpRequestDto.builder()
                .name("김민우1234")
                .password("anstn1234@")
                .userId(userId)
                .build();

    }

    public static MemeberRequestDto.MemberUpdatePasswordRequestDto updatePassword(String mail, String newPassword, String oldPassword) {
        return MemeberRequestDto.MemberUpdatePasswordRequestDto.builder()
                .newPassword(newPassword)
                .oldPassword(oldPassword)
                .userId(mail)
                .build();
    }

    public static MemeberRequestDto.RegisterEmailRequestDto registerEmailRequestDto(String email) {
        return MemeberRequestDto.RegisterEmailRequestDto.builder()
                .email(email)
                .build();
    }
}
