package com.example.rework.auth.service;

import com.example.rework.auth.entity.RefreshToken;
import com.example.rework.auth.repository.RefreshTokenRepository;
import com.example.rework.member.domain.Member;
import com.example.rework.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public void setRefreshToken(String username, String refreshToken) {
        Member member = memberRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("No member found for the given username"));
        refreshTokenRepository.save(RefreshToken.builder()
                .token(refreshToken)
                .id(member.getUserId())
                .build()
        );
    }
}