package com.example.rework.dailyagenda.application.impl;

import com.example.rework.config.security.SecurityUtils;
import com.example.rework.dailyagenda.application.DailyAgendaService;
import com.example.rework.dailyagenda.application.dto.DailyAgendaRequestDto.*;
import com.example.rework.dailyagenda.application.dto.DailyAgendaResponseDto.*;
import com.example.rework.dailyagenda.domain.DailyAgenda;
import com.example.rework.dailyagenda.domain.repository.DailyAgendaRepository;
import com.example.rework.global.error.NotFoundAccountException;
import com.example.rework.global.error.NotFoundAgendaException;
import com.example.rework.global.error.UnAuthorizedException;
import com.example.rework.member.domain.Member;
import com.example.rework.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DailyAgendaServiceImpl implements DailyAgendaService {
    private final DailyAgendaRepository dailyAgendaRepository;
    private final MemberRepository memberRepository;

    @Override
    public ReadDailyAgendaResponseDto readDailyAgenda(ReadDailyAgendaRequestDto readDailyAgendaRequestDto, SecurityUtils securityUtils) {
        Optional<Member> curMember = memberRepository.findByUserId(securityUtils.getCurrentUserId());
        Long currentUserId = curMember.get().getId();

        LocalDate date = LocalDate.of(readDailyAgendaRequestDto.getYear(), readDailyAgendaRequestDto.getMonth(), readDailyAgendaRequestDto.getDay());
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<DailyAgenda> agendaList;

        if (readDailyAgendaRequestDto.getState() == null) {
            // 모든 투두 조회
            agendaList = dailyAgendaRepository.findByMemberIdAndCreatedAtBetween(currentUserId, start, end);
        } else {
            // 완료된 투두 또는 미완료 투두 조회
            agendaList = dailyAgendaRepository.findByMemberIdAndCreatedAtBetweenAndState(currentUserId, start, end, readDailyAgendaRequestDto.getState());
        }

        List<ReadDetailDailyAgendaResponseDto> result = agendaList.stream()
                .map(agenda -> new ReadDetailDailyAgendaResponseDto(agenda.getId(), agenda.getTodo(), agenda.isState())).toList();

        return new ReadDailyAgendaResponseDto(result);
    }

    @Override
    @Transactional
    public CreateDailyAgendaResponseDto createDailyAgenda(CreateDailyAgendaRequestDto createDailyAgendaRequestDto, SecurityUtils securityUtils) {
        Optional<Member> curMember = memberRepository.findByUserId(securityUtils.getCurrentUserId());
        Long currentUserId = curMember.get().getId();

        Member member = memberRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundAccountException("Member not found with id: " + currentUserId));

        DailyAgenda dailyAgenda = DailyAgenda.builder()
                .member(member)
                .todo(createDailyAgendaRequestDto.getTodo())
                .state(false)
                .build();

        DailyAgenda result = dailyAgendaRepository.save(dailyAgenda);

        return CreateDailyAgendaResponseDto.builder()
                .agendaId(result.getId())
                .todo(result.getTodo())
                .build();
    }

    @Override
    @Transactional
    public UpdateDailyAgendaResponseDto updateDailyAgenda(UpdateDailyAgendaRequestDto updateDailyAgendaRequestDto, SecurityUtils securityUtils) {
        Optional<Member> curMember = memberRepository.findByUserId(securityUtils.getCurrentUserId());
        Long currentUserId = curMember.get().getId();

        Member member = memberRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundAccountException("Member not found with id: " + currentUserId));
        DailyAgenda dailyAgenda = dailyAgendaRepository.findById(updateDailyAgendaRequestDto.getAgendaId())
                .orElseThrow(() -> new NotFoundAgendaException("agenda not found with id:" + updateDailyAgendaRequestDto.getAgendaId()));

        if (!dailyAgenda.getMember().getId().equals(currentUserId)) {
            throw new UnAuthorizedException("유저가 소유한 아젠다가 아닙니다.");
        }
        dailyAgenda.setTodo(updateDailyAgendaRequestDto.getTodo());
        dailyAgenda.setState(updateDailyAgendaRequestDto.isState());

        dailyAgendaRepository.save(dailyAgenda);

        return UpdateDailyAgendaResponseDto.builder()
                .agendaId(dailyAgenda.getId())
                .todo(dailyAgenda.getTodo())
                .state(dailyAgenda.isState())
                .build();
    }

    @Override
    @Transactional
    public boolean deleteDailyAgenda(Long dailyAgendaId, SecurityUtils securityUtils) {
        Optional<Member> curMember = memberRepository.findByUserId(securityUtils.getCurrentUserId());
        Long currentUserId = curMember.get().getId();

        Member member = memberRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundAccountException("Member not found with id: " + currentUserId));
        DailyAgenda dailyAgenda = dailyAgendaRepository.findById(dailyAgendaId)
                .orElseThrow(() -> new NotFoundAgendaException("agenda not found with id:" + dailyAgendaId));

        if (!dailyAgenda.getMember().getId().equals(currentUserId)) {
            throw new UnAuthorizedException("유저가 소유한 아젠다가 아닙니다.");
        }

        dailyAgendaRepository.deleteById(dailyAgenda.getId());

        return true;
    }
}
