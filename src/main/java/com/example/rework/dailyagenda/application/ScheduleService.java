package com.example.rework.dailyagenda.application;

import com.example.rework.dailyagenda.domain.DailyAgenda;
import com.example.rework.dailyagenda.domain.repository.DailyAgendaRepository;
import com.example.rework.member.domain.Member;
import com.example.rework.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ScheduleService {
    private MemberRepository memberRepository;
    private DailyAgendaRepository dailyAgendaRepository;

    // 처음에는 미완료한 아젠다가 계속 다음날로 넘어감
    // 하루기준 전날 미완료 한거만 오늘 아젠다에 복사한다.
    // 전날 못한거 조회는 해야된다.
    // 그래서 복사를 하고있는것.
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul") // 매일 자정 실행
    public void copyInCompleteAgendaToNextDay() {
        List<Member> allMemberList = memberRepository.findAll();

        LocalDateTime now = LocalDateTime.now();
        LocalDate yesterday = now.toLocalDate().minusDays(1);
        LocalDateTime startOfYesterday = yesterday.atStartOfDay();
        LocalDateTime endOfYesterday = yesterday.atTime(LocalTime.MAX);
        // LocalDateTime nextDayStart = now.toLocalDate().plusDays(1).atStartOfDay();
        List<DailyAgenda> result = new ArrayList<>();

        for (Member member : allMemberList) {
            Long memberId = member.getId();
            List<DailyAgenda> inCompleteAgendaList =
                    dailyAgendaRepository.findByMemberIdAndCreatedAtBetweenAndState(memberId, startOfYesterday, endOfYesterday, false);
            for (DailyAgenda agenda : inCompleteAgendaList) {
                DailyAgenda newAgenda = DailyAgenda.builder()
                        .member(agenda.getMember())
                        .id(agenda.getId())
                        .state(agenda.isState())
                        .todo(agenda.getTodo())
                        .build();
                result.add(newAgenda);
            }
        }
        dailyAgendaRepository.saveAll(result);
    }
}
