package com.example.rework.dailyagenda.application.impl;

import com.example.rework.auth.MemberRole;
import com.example.rework.auth.jwt.MemberDetails;
import com.example.rework.dailyagenda.application.dto.DailyAgendaRequestDto;
import com.example.rework.dailyagenda.application.dto.DailyAgendaResponseDto;
import com.example.rework.dailyagenda.domain.DailyAgenda;
import com.example.rework.dailyagenda.domain.repository.DailyAgendaRepository;
import com.example.rework.global.error.AlreadyPagingIdException;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DailyAgendaServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private DailyAgendaRepository dailyAgendaRepository;

    @InjectMocks
    private DailyAgendaServiceImpl dailyAgendaService;

    @BeforeEach
    void SecurityUserTest() {
        Member member = getMember();

        MemberDetails memberDetails = new MemberDetails(member);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("오늘의 아젠다를 생성하면 정보를 반환한다.")
    void createDailyAgenda() {
        DailyAgendaRequestDto.CreateDailyAgendaRequestDto createDailyAgendaRequestDto = getDailyAgendaCreateReq();

        LocalDateTime startOfDay = createDailyAgendaRequestDto.getCreatedAt().atStartOfDay();
        LocalDateTime endOfDay = createDailyAgendaRequestDto.getCreatedAt().atTime(LocalTime.MAX);

        given(memberRepository.findByUserId(any()))
                .willReturn(Optional.ofNullable(getMember()));

        given(memberRepository.findById(any()))
                .willReturn(Optional.ofNullable(getMember()));
        given(dailyAgendaRepository.save(any(DailyAgenda.class)))
                .willReturn(getDailyAgenda());

        //when
        DailyAgendaResponseDto.CreateDailyAgendaResponseDto createDailyAgendaResponseDto = dailyAgendaService.createDailyAgenda(createDailyAgendaRequestDto, null);

        //then
        Assertions.assertThat(createDailyAgendaResponseDto.getAgendaId()).isEqualTo(1L);
        Assertions.assertThat(createDailyAgendaResponseDto.getTodo()).isEqualTo("오늘의 아젠다");

    }

    @Test
    @DisplayName("오늘의 아젠다 조회")
    void readDailyAgenda() {
        LocalDate Today = LocalDate.now();
        int year = Today.getYear();
        int month = Today.getMonthValue();
        int day = Today.getDayOfMonth();
        boolean state = false;

        Long userId = 1L;
        LocalDate date = LocalDate.of(year, month, day);
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        List<DailyAgenda> dailyAgendaList = getDailyAgendaList(userId);

        given(memberRepository.findByUserId(any()))
                .willReturn(Optional.ofNullable(getMember()));

        given(dailyAgendaRepository.findByMemberIdAndCreatedAtBetweenAndState(userId, start, end, state))
                .willReturn(dailyAgendaList);

        DailyAgendaResponseDto.ReadDailyAgendaResponseDto readDailyAgendaResponseDto = dailyAgendaService.readDailyAgenda(year, month, day, state, null);

        // then
        Assertions.assertThat(readDailyAgendaResponseDto.getAgendaList().get(0).getPagingId()).isEqualTo(1L);
        Assertions.assertThat(readDailyAgendaResponseDto.getAgendaList().get(0).getTodo()).isEqualTo("test1");
        Assertions.assertThat(readDailyAgendaResponseDto.getAgendaList().get(0).isState()).isFalse();

        Assertions.assertThat(readDailyAgendaResponseDto.getAgendaList().get(1).getPagingId()).isEqualTo(2L);
        Assertions.assertThat(readDailyAgendaResponseDto.getAgendaList().get(1).getTodo()).isEqualTo("test2");
        Assertions.assertThat(readDailyAgendaResponseDto.getAgendaList().get(1).isState()).isFalse();
    }

    @Test
    @DisplayName("오늘의 아젠다 수정")
    void updateDailyAgenda() {
        //given
        DailyAgendaRequestDto.UpdateDailyAgendaRequestDto updateDailyAgendaRequestDto = getDailyAgendaUpdateReq();

        given(memberRepository.findByUserId(any()))
                .willReturn(Optional.ofNullable(getMember()));

        given(memberRepository.findById(any()))
                .willReturn(Optional.ofNullable(getMember()));

        given(dailyAgendaRepository.findById(any()))
                .willReturn(Optional.ofNullable(getDailyAgenda()));

        //when
        DailyAgendaResponseDto.UpdateDailyAgendaResponseDto updateDailyAgendaResponseDto = dailyAgendaService.updateDailyAgenda(updateDailyAgendaRequestDto, null);

        //then
        Assertions.assertThat(updateDailyAgendaResponseDto.getAgendaId()).isEqualTo(1L);
        Assertions.assertThat(updateDailyAgendaResponseDto.getTodo()).isEqualTo("수정된 아젠다");
        Assertions.assertThat(updateDailyAgendaResponseDto.getPagingId()).isEqualTo(2L);
    }


    @Test
    @DisplayName("오늘의 아젠다 삭제")
    void deleteDailyAgenda() {
        //given
        given(memberRepository.findByUserId(any()))
                .willReturn(Optional.ofNullable(getMember()));

        given(memberRepository.findById(any()))
                .willReturn(Optional.ofNullable(getMember()));
        given(dailyAgendaRepository.findById(any()))
                .willReturn(Optional.ofNullable(getDailyAgenda()));

        //when
        boolean result = dailyAgendaService.deleteDailyAgenda(getDailyAgenda().getId(), null);

        //then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("pagingId가 중복되지 않을 때 아젠다 일괄 업데이트 테스트")
    void bulkUpdateDailyAgendaByPagingId_Success() {
        // given
        Long userId = 1L;
        Long firstPagingId = 5L;
        Long secondPagingId = 4L;
        LocalDate createdAt = LocalDate.of(2024, 5, 29);
        List<DailyAgendaRequestDto.UpdateDailyAgendaListRequestDto> updateDailyAgendaListRequestDtoList = getUpdateDailyAgendaListRequestDtos(createdAt, firstPagingId, secondPagingId);
        List<DailyAgenda> dailyAgendaList = getDailyAgendaList(userId);

        // mock 객체 설정
        given(memberRepository.findByUserId(any())).willReturn(Optional.ofNullable(getMember()));
        given(dailyAgendaRepository.findByMemberIdAndCreatedAtBetween(userId,
                LocalDateTime.of(2024, 5, 29, 0, 0),
                LocalDateTime.of(2024, 5, 29, 23, 59, 59, 999_999_999))).willReturn(dailyAgendaList);

        given(dailyAgendaRepository.saveAll(dailyAgendaList)).willReturn(dailyAgendaList);

        // when
        List<DailyAgendaResponseDto.ReadDetailDailyAgendaResponseDto> readDetailDailyAgendaResponseDtos = dailyAgendaService.bulkUpdateDailyAgendaByPagingId(updateDailyAgendaListRequestDtoList, null);


        // then
        Assertions.assertThat(readDetailDailyAgendaResponseDtos.get(0).getPagingId()).isEqualTo(firstPagingId);
        Assertions.assertThat(readDetailDailyAgendaResponseDtos.get(0).getTodo()).isEqualTo("test1");
        Assertions.assertThat(readDetailDailyAgendaResponseDtos.get(0).isState()).isFalse();

        Assertions.assertThat(readDetailDailyAgendaResponseDtos.get(1).getPagingId()).isEqualTo(secondPagingId);
        Assertions.assertThat(readDetailDailyAgendaResponseDtos.get(1).getTodo()).isEqualTo("test2");
        Assertions.assertThat(readDetailDailyAgendaResponseDtos.get(1).isState()).isFalse();

    }


    @Test
    @DisplayName("pagingId가 중복됐을 경우 에러를 발생시킨다.")
    void bulkUpdateDailyAgendaByPagingId_DuplicatePagingId_Error() {
        // given
        Long userId = 1L;
        Long firstPagingId = 5L;
        Long secondPagingId = 5L;
        LocalDate createdAt = LocalDate.of(2024, 5, 29);
        List<DailyAgendaRequestDto.UpdateDailyAgendaListRequestDto> updateDailyAgendaListRequestDtoList = getUpdateDailyAgendaListRequestDtos(createdAt, firstPagingId, secondPagingId);
        List<DailyAgenda> dailyAgendaList = getDailyAgendaList(userId);

        // mock 객체 설정
        given(memberRepository.findByUserId(any())).willReturn(Optional.ofNullable(getMember()));
        //when&then
        assertThrows(AlreadyPagingIdException.class, () -> {
            dailyAgendaService.bulkUpdateDailyAgendaByPagingId(updateDailyAgendaListRequestDtoList, any());
        });
    }

    private DailyAgendaRequestDto.CreateDailyAgendaRequestDto getDailyAgendaCreateReq() {
        return DailyAgendaRequestDto.CreateDailyAgendaRequestDto.builder()
                .pagingId(1L)
                .todo("오늘의 아젠다")
                .createdAt(LocalDate.parse("2024-05-30"))
                .build();
    }

    private DailyAgendaRequestDto.UpdateDailyAgendaRequestDto getDailyAgendaUpdateReq() {
        return DailyAgendaRequestDto.UpdateDailyAgendaRequestDto.builder()
                .agendaId(1L)
                .todo("수정된 아젠다")
                .pagingId(2L)
                .state(true)
                .build();
    }

    private DailyAgenda getDailyAgenda() {
        DailyAgenda dailyAgenda = DailyAgenda.builder()
                .todo("오늘의 아젠다")
                .pagingId(1L)
                .state(false)
                .member(getMember())
                .build();
        ReflectionTestUtils.setField(dailyAgenda, "id", 1L);
        return dailyAgenda;
    }

    private static List<DailyAgenda> getDailyAgendaList(Long userId) {
        Member member = Member.builder()
                .id(userId)
                .build();
        DailyAgenda dailyAgenda1 = DailyAgenda.builder()
                .id(1L)
                .todo("test1")
                .state(false)
                .pagingId(1L)
                .member(member)
                .build();
        DailyAgenda dailyAgenda2 = DailyAgenda.builder()
                .id(2L)
                .todo("test2")
                .state(false)
                .pagingId(2L)
                .member(member)
                .build();
        List<DailyAgenda> dailyAgendaList = new ArrayList<>();

        dailyAgendaList.add(dailyAgenda1);
        dailyAgendaList.add(dailyAgenda2);
        return dailyAgendaList;
    }

    private static List<DailyAgendaRequestDto.UpdateDailyAgendaListRequestDto> getUpdateDailyAgendaListRequestDtos(LocalDate createdAt, Long firstPagingId, Long secondPagingId) {
        List<DailyAgendaRequestDto.UpdateDailyAgendaListRequestDto> updateDailyAgendaListRequestDtoList = new ArrayList<>();
        updateDailyAgendaListRequestDtoList.add(DailyAgendaRequestDto.UpdateDailyAgendaListRequestDto.builder()
                .agendaId(1L)
                .todo("test1")
                .state(false)
                .pagingId(firstPagingId)
                .createdAt(createdAt)
                .build());
        updateDailyAgendaListRequestDtoList.add(DailyAgendaRequestDto.UpdateDailyAgendaListRequestDto.builder()
                .agendaId(2L)
                .todo("test2")
                .state(false)
                .pagingId(secondPagingId)
                .createdAt(createdAt)
                .build());
        return updateDailyAgendaListRequestDtoList;
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

}