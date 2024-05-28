package com.example.rework.dailyagenda.application;

import com.example.rework.auth.MemberRole;
import com.example.rework.dailyagenda.domain.DailyAgenda;
import com.example.rework.dailyagenda.domain.repository.DailyAgendaRepository;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private DailyAgendaRepository dailyAgendaRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    private Member member;
    private List<Member> memberList = new ArrayList<>();
    private DailyAgenda incompleteAgenda;
    private List<DailyAgenda> incompleteAgendaList = new ArrayList<>();
    private List<DailyAgenda> incompleteAgendaTodayList = new ArrayList<>();

    // 전날 아젠다리스트를 로직을 돌리면
    // 오늘 아젠다리스트에 그 값들이 있겠지?
    // 그런데 이제 전날리스트는 그대로있고
    // 새로 생성된 아젠다리스트가 오늘 리스트에 생긴다.

    @BeforeEach
    void setUp() {
        Member member1 = Member.builder()
                .role(MemberRole.MEMBER)
                .id(1L)
                .name("민우")
                .password("test1234")
                .userId("kbsserver@naver.com").build();
        memberList.add(member1);

        for (int i = 1; i <= 5; i++) {
            DailyAgenda dailyAgenda = DailyAgenda.builder()
                    .id((long) i)
                    .todo("오늘의 아젠다" + i)
                    .member(member1)
                    .state(i % 2 != 0)
                    .build();
            //ReflectionTestUtils.setField(dailyAgenda, "createdAt", LocalDateTime.now().minusDays(1));
            incompleteAgendaList.add(dailyAgenda);
        }

        for (int i = 1; i <= 5; i++) {
            DailyAgenda dailyAgenda = DailyAgenda.builder()
                    .id((long) i)
                    .todo("오늘의 아젠다" + i)
                    .member(member1)
                    .state(i % 2 != 0)
                    .build();
            ReflectionTestUtils.setField(dailyAgenda, "createdAt", LocalDateTime.now());
            if (i % 2 == 0) //false 인거만 담김
                incompleteAgendaTodayList.add(dailyAgenda);
        }

    }

    @DisplayName("미완료 아젠다가 다음날로 복제된다.")
    @Test
    void copyInCompleteAgendaToNextDay() {
        final LocalDateTime cur = LocalDateTime.now();

        given(memberRepository.findAll()).willReturn(memberList);

        given(dailyAgendaRepository.findByMemberIdAndCreatedAtBetweenAndState(
                eq(getMember().getId()),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                eq(false)
        )).willReturn(incompleteAgendaList);

        //  (오늘)미완료된 아젠다 리스트를, 다음날로 복사하는 로직.
        given(dailyAgendaRepository.saveAll(any())).willReturn(incompleteAgendaTodayList);

        scheduleService.copyInCompleteAgendaToNextDay();

        for (DailyAgenda agendaToday : incompleteAgendaTodayList) {
            for (DailyAgenda agendaYesterday : incompleteAgendaList) {
                if (agendaToday.getTodo().equals(agendaYesterday.getTodo())) {
                    Assertions.assertThat(agendaToday.getMember().getId()).isEqualTo(agendaYesterday.getMember().getId());
                    Assertions.assertThat(agendaToday.isState()).isEqualTo(agendaYesterday.isState());
                    // 다음날로 복제된 아젠다의 createdAt이 오늘로 설정되었는지 확인
                    Assertions.assertThat(agendaToday.getCreatedAt().toLocalDate()).isEqualTo(LocalDateTime.now().toLocalDate());
                }
            }
        }
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

    private DailyAgenda getDailyAgenda() {
        DailyAgenda dailyAgenda = DailyAgenda.builder()
                .todo("오늘의 아젠다")
                .state(false)
                .member(getMember())
                .build();
        ReflectionTestUtils.setField(dailyAgenda, "id", 1L);

        return dailyAgenda;
    }
}
