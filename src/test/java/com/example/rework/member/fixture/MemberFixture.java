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
}
