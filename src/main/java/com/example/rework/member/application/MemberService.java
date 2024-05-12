package com.example.rework.member.application;

import com.example.rework.member.application.dto.MemberResponseDto;
import com.example.rework.member.application.dto.MemeberRequestDto;
import com.example.rework.member.domain.Member;
import org.springframework.security.core.Authentication;

public interface MemberService {
    MemberResponseDto.MemberCreateResponseDto createMember(MemeberRequestDto.SignUpRequestDto signUpRequestDto);
    Member findMemberByUserId(String username);
    String renewAccessToken(String cookieRefreshToken, Authentication authentication);
    void memberLogout(String username);
}
