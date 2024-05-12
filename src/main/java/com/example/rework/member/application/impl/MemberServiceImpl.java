package com.example.rework.member.application.impl;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.rework.auth.entity.RefreshToken;
import com.example.rework.auth.jwt.JwtProvider;
import com.example.rework.auth.repository.RefreshTokenRepository;
import com.example.rework.global.error.DuplicateAccountException;
import com.example.rework.global.error.InvalidTokenException;
import com.example.rework.member.application.MemberService;
import com.example.rework.member.application.dto.MemberResponseDto;
import com.example.rework.member.application.dto.MemeberRequestDto;
import com.example.rework.member.domain.Member;
import com.example.rework.member.domain.repository.MemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public MemberResponseDto.MemberCreateResponseDto createMember(MemeberRequestDto.SignUpRequestDto signUpRequestDto) {
        //중복체크검사
        signupVaidate(signUpRequestDto);
        //패스워드 인코딩
        MemeberRequestDto.SignUpRequestDto encodingDto = encodingPassword(signUpRequestDto);
        //회원 저장
        Member member = memberRepository.save(encodingDto.toEntity());
        return MemberResponseDto.MemberCreateResponseDto.builder()
                .memberRole(String.valueOf(member.getRole()))
                .userId(member.getUserId())
                .memberId(member.getId())
                .name(member.getName())
                .build();
    }

    @Override
    @Transactional
    public String renewAccessToken(String refreshToken,Authentication authentication) {
        log.info("refreshToken = " + refreshToken);
        if (!jwtProvider.verifyToken(refreshToken)) {
            throw new InvalidTokenException();
        }
        String username = jwtProvider.getUsernameFromToken(refreshToken);

        RefreshToken refreshTokenFound = refreshTokenRepository.findById(username).orElseThrow(()->new UsernameNotFoundException("no member by username"));

        if (!refreshTokenFound.getToken().equals(refreshToken)) {
            throw new JWTVerificationException("not matching refreshToken");
        }
        return jwtProvider.generateAccessToken(username,authentication);
    }

    @Override
    @Transactional
    public void memberLogout(String username) {
        refreshTokenRepository.deleteById(username);
    }


    @Override
    public Member findMemberByUserId(String username) {
        Member member = memberRepository.findByUserId(username).orElseThrow(() -> new UsernameNotFoundException("no member by username"));
        return member;
    }

    /**
     * 패스워드 암호화
     */
    private MemeberRequestDto.SignUpRequestDto encodingPassword(MemeberRequestDto.SignUpRequestDto signUpRequestDto){
        final String password=signUpRequestDto.getPassword();
        signUpRequestDto.setPassword(bCryptPasswordEncoder.encode(password));
        return signUpRequestDto;
    }

    /**
     * 회원유효성검증
     */
    private void signupVaidate(MemeberRequestDto.SignUpRequestDto signUpRequestDto){
        String userId = signUpRequestDto.getUserId();
        if (memberRepository.existsByUserId(userId)) {
            throw new DuplicateAccountException("아이디 중복");
        }
    }
}
