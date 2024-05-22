package com.example.rework.monthlyagenda.application.impl;

import com.example.rework.MonthlyAgenda.application.dto.MonthlyAgendaRequestDto;
import com.example.rework.MonthlyAgenda.application.dto.MonthlyAgendaResponseDto;
import com.example.rework.MonthlyAgenda.application.impl.MonthlyAgendaServiceImpl;
import com.example.rework.MonthlyAgenda.domain.MonthlyAgenda;
import com.example.rework.MonthlyAgenda.domain.repository.MonthlyAgendaRepository;
import com.example.rework.auth.MemberRole;
import com.example.rework.auth.jwt.MemberDetails;
import com.example.rework.config.security.SecurityUtils;
import com.example.rework.global.base.BaseTimeEntity;
import com.example.rework.member.domain.Member;
import com.example.rework.member.domain.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MonthlyAgendaServiceImplTest {
    @InjectMocks
    private MonthlyAgendaServiceImpl monthlyAgendaService;
    @Mock
    private MonthlyAgendaRepository monthlyAgendaRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private SecurityUtils securityUtils;
    @Mock
    private BaseTimeEntity baseTimeEntity;

    @BeforeEach
    void SecurityUserTest() {
        Member member = getMember();

        MemberDetails memberDetails = new MemberDetails(member);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @DisplayName("이번달 아젠다 생성하게 되면 해당 정보를 반환한다.")
    @Test
    void createMonthlyAgenda() {
        //given
        MonthlyAgendaRequestDto.CreateMonthlyAgendaRequestDto createMonthlyAgendaRequestDto = getMonthlyAgendaCreateReq();

        given(memberRepository.findByUserId(any()))
                .willReturn(Optional.ofNullable(getMember()));

        given(memberRepository.findById(any()))
                .willReturn(Optional.ofNullable(getMember()));

        given(monthlyAgendaRepository.save(any(MonthlyAgenda.class)))
                .willReturn(getMonthlyAgenda());

        //when
        MonthlyAgendaResponseDto.CreateMonthlyAgendaResponseDto createMonthlyAgendaResponseDto = monthlyAgendaService.createMonthlyAgenda(createMonthlyAgendaRequestDto, securityUtils);

        //then
        Assertions.assertThat(createMonthlyAgendaResponseDto.getAgendaId()).isEqualTo(1L);
        Assertions.assertThat(createMonthlyAgendaResponseDto.getTodo()).isEqualTo("이번달 아젠다");
    }

    @DisplayName("이번달 아젠다를 조회할 수 있다.")
    @Test
    void readMonthlyAgenda() {
        //given
        MonthlyAgendaRequestDto.ReadMonthlyAgendaRequestDto readMonthlyAgendaRequestDto = getMonthlyAgendaReadReq();

        given(memberRepository.findByUserId(any()))
                .willReturn(Optional.ofNullable(getMember()));

        given(monthlyAgendaRepository.findByMemberIdAndCreatedAtBetween(any(), any(), any()))
                .willReturn(Optional.ofNullable(getMonthlyAgenda()));

        //when
        MonthlyAgendaResponseDto.ReadMonthlyAgendaResponseDto readMonthlyAgendaResponseDto = monthlyAgendaService.readMonthlyAgenda(readMonthlyAgendaRequestDto, securityUtils);

        //then

        Assertions.assertThat(readMonthlyAgendaResponseDto.getAgendaId()).isEqualTo(1L);
        Assertions.assertThat(readMonthlyAgendaResponseDto.getTodo()).isEqualTo("이번달 아젠다");
        Assertions.assertThat(readMonthlyAgendaResponseDto.isState()).isFalse();
        Assertions.assertThat(readMonthlyAgendaResponseDto.getCreateTime()).isEqualTo(getMonthlyAgenda().getCreatedAt());
    }

    @DisplayName("이번달 아젠다를 수정할 수 있다.")
    @Test
    void updateMonthlyAgenda() {
        //given
        MonthlyAgendaRequestDto.UpdateMonthlyAgendaRequestDto updateMonthlyAgendaRequestDto = getMonthlyAgendaUpdateReq();

        given(memberRepository.findByUserId(any()))
                .willReturn(Optional.ofNullable(getMember()));

        given(memberRepository.findById(any()))
                .willReturn(Optional.ofNullable(getMember()));

        given(monthlyAgendaRepository.findById(any()))
                .willReturn(Optional.ofNullable(getMonthlyAgenda()));

        //when
        MonthlyAgendaResponseDto.UpdateMonthlyAgendaResponseDto updateMonthlyAgendaResponseDto = monthlyAgendaService.updateMonthlyAgenda(updateMonthlyAgendaRequestDto, securityUtils);

        //then
        Assertions.assertThat(updateMonthlyAgendaResponseDto.getAgendaId()).isEqualTo(1L);
        Assertions.assertThat(updateMonthlyAgendaResponseDto.getTodo()).isEqualTo("수정된 아젠다");
        Assertions.assertThat(updateMonthlyAgendaResponseDto.isState()).isFalse();
    }

    @DisplayName("이번달 아젠다를 삭제할 수 있다.")
    @Test
    void deleteMonthlyAgenda() {
        //given
        given(memberRepository.findByUserId(any()))
                .willReturn(Optional.ofNullable(getMember()));

        given(memberRepository.findById(any()))
                .willReturn(Optional.ofNullable(getMember()));

        given(monthlyAgendaRepository.findById(any()))
                .willReturn(Optional.ofNullable(getMonthlyAgenda()));

        //when
        boolean result = monthlyAgendaService.deleteMonthlyAgenda(getMonthlyAgenda().getId(), securityUtils);

        //then
        Assertions.assertThat(result).isTrue();
    }

    private MonthlyAgendaRequestDto.CreateMonthlyAgendaRequestDto getMonthlyAgendaCreateReq() {
        return MonthlyAgendaRequestDto.CreateMonthlyAgendaRequestDto.builder()
                .todo("이번달 아젠다")
                .build();
    }

    private MonthlyAgendaRequestDto.ReadMonthlyAgendaRequestDto getMonthlyAgendaReadReq() {
        return MonthlyAgendaRequestDto.ReadMonthlyAgendaRequestDto.builder()
                .year(2024)
                .month(5)
                .build();
    }

    private MonthlyAgendaRequestDto.UpdateMonthlyAgendaRequestDto getMonthlyAgendaUpdateReq() {
        return MonthlyAgendaRequestDto.UpdateMonthlyAgendaRequestDto.builder()
                .agendaId(1L)
                .todo("수정된 아젠다")
                .build();
    }

    private Member getMember() {
        Member member = Member.builder()
                .role(MemberRole.MEMBER)
                .name("민우")
                .password("test1234")
                .userId("kbsserver@naver.com")
                .build();
        ReflectionTestUtils.setField(member, "id", 1L);
        return member;
    }

    private MonthlyAgenda getMonthlyAgenda() {
        MonthlyAgenda monthlyAgenda = MonthlyAgenda.builder()
                .todo("이번달 아젠다")
                .state(false)
                .member(getMember())
                .build();
        ReflectionTestUtils.setField(monthlyAgenda, "id", 1L);

        return monthlyAgenda;
    }

}
