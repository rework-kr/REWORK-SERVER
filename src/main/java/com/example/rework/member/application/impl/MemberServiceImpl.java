package com.example.rework.member.application.impl;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.rework.auth.entity.RefreshToken;
import com.example.rework.auth.jwt.JwtProvider;
import com.example.rework.auth.repository.RefreshTokenRepository;
import com.example.rework.config.security.SecurityUtils;
import com.example.rework.discord.WebhookService;
import com.example.rework.global.error.DuplicateAccountException;
import com.example.rework.global.error.InvalidTokenException;
import com.example.rework.global.error.PasswordNotMatchException;
import com.example.rework.global.error.PasswordUnchangedException;
import com.example.rework.member.application.MemberService;
import com.example.rework.member.application.dto.MemberResponseDto;
import com.example.rework.member.application.dto.MemeberRequestDto;
import com.example.rework.member.application.dto.MemeberRequestDto.SignUpRequestDto;
import com.example.rework.member.domain.Member;
import com.example.rework.member.domain.NonMemberEmail;
import com.example.rework.member.domain.repository.MemberRepository;
import com.example.rework.member.domain.repository.NonMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final NonMemberRepository nonMemberRepository;
    private final WebhookService webhookService;

    @Override
    @Transactional
    public MemberResponseDto.MemberCreateResponseDto createMember(SignUpRequestDto signUpRequestDto) {
        //중복체크검사
        signupVaidate(signUpRequestDto);
        //패스워드 인코딩
        SignUpRequestDto encodingDto = encodingPassword(signUpRequestDto);
        //회원 저장
        Member member = memberRepository.save(encodingDto.toEntity());
        NonMemberEmail nonMemberEmail = nonMemberRepository.findByEmail(member.getUserId());
        nonMemberEmail.updateIsAccepted(true);
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
    @Transactional
    public boolean updatePassword(MemeberRequestDto.MemberUpdatePasswordRequestDto updatePasswordRequestDto, SecurityUtils securityUtils) {
        Optional<Member> curMember = memberRepository.findByUserId(securityUtils.getCurrentUserId());
        String newPassword = updatePasswordRequestDto.getNewPassword();
        String oldPassword = updatePasswordRequestDto.getOldPassword();

        if(newPassword.equals(oldPassword)){
            throw new PasswordUnchangedException("기존 패스워드와 동일합니다.");
        }
        if(!bCryptPasswordEncoder.matches(oldPassword,curMember.get().getPassword())){
            throw new PasswordNotMatchException("패스워드가 일치하지 않습니다.");
        }

        curMember.get().updatePassword(bCryptPasswordEncoder.encode(newPassword));
        return true;
    }

    @Override
    @Transactional
    public boolean registerEmail(MemeberRequestDto.RegisterEmailRequestDto registerEmailRequestDto) {
        String email = registerEmailRequestDto.getEmail();
        NonMemberEmail nonMemberEmail = NonMemberEmail.builder()
                .email(email)
                .build();

        nonMemberRepository.save(nonMemberEmail);
        webhookService.sendDiscordNotificationForNonMemberRegister(email);

        return true;
    }

    @Override
    public List<MemberResponseDto.NonMemberEmailListResponseDto> adminNonMemberEamilList(SecurityUtils securityUtils) {
        Optional<Member> curMember = memberRepository.findByUserId(securityUtils.getCurrentUserId());
        // 회원가입 승인 대기중인 이메일 리스트
        List<NonMemberEmail> nonMemberEmailList = nonMemberRepository.findAllByIsAccepted(false);

        List<MemberResponseDto.NonMemberEmailListResponseDto> result = nonMemberEmailList.stream().map(MemberResponseDto.NonMemberEmailListResponseDto::new).collect(Collectors.toList());
        return result;
    }

    @Override
    public MemberResponseDto.MemberInfoResponseDto readMemberInfo(SecurityUtils securityUtils) {
        Optional<Member> curMember = memberRepository.findByUserId(securityUtils.getCurrentUserId());
        Member member = curMember.get();

        return MemberResponseDto.MemberInfoResponseDto.builder()
                .initialPasswordUpdateState(member.isInitialPasswordState())
                .memberRole(member.getRole())
                .email(member.getUserId())
                .build();
    }


    @Override
    public Member findMemberByUserId(String username) {
        Member member = memberRepository.findByUserId(username).orElseThrow(() -> new UsernameNotFoundException("no member by username"));
        return member;
    }

    /**
     * 패스워드 암호화
     */
    private SignUpRequestDto encodingPassword(SignUpRequestDto signUpRequestDto){
        final String password=signUpRequestDto.getPassword();
        signUpRequestDto.setPassword(bCryptPasswordEncoder.encode(password));
        return signUpRequestDto;
    }

    /**
     * 회원유효성검증
     */
    private void signupVaidate(SignUpRequestDto signUpRequestDto){
        String userId = signUpRequestDto.getUserId();
        if (memberRepository.existsByUserId(userId)) {
            throw new DuplicateAccountException("아이디 중복");
        }
    }
}
