package com.example.rework.member.application;

import com.example.rework.config.security.SecurityUtils;
import com.example.rework.member.application.dto.MemberResponseDto;
import com.example.rework.member.application.dto.MemberResponseDto.MemberInfoResponseDto;
import com.example.rework.member.application.dto.MemeberRequestDto;
import com.example.rework.member.domain.Member;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface MemberService {
    MemberResponseDto.MemberCreateResponseDto createMember(MemeberRequestDto.SignUpRequestDto signUpRequestDto);
    Member findMemberByUserId(String username);
    String renewAccessToken(String cookieRefreshToken, Authentication authentication);
    void memberLogout(String username);

    boolean updatePassword(MemeberRequestDto.MemberUpdatePasswordRequestDto updatePasswordRequestDto, SecurityUtils securityUtils);

    boolean registerEmail(MemeberRequestDto.RegisterEmailRequestDto registerEmailRequestDto);

    List<MemberResponseDto.NonMemberEmailListResponseDto> adminNonMemberEamilList(SecurityUtils securityUtils);

    MemberInfoResponseDto readMemberInfo(SecurityUtils securityUtils);
}
