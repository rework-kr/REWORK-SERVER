package com.example.rework.MonthlyAgenda.application.impl;

import com.example.rework.MonthlyAgenda.application.MonthlyAgendaService;
import com.example.rework.MonthlyAgenda.application.dto.MonthlyAgendaRequestDto.CreateMonthlyAgendaRequestDto;
import com.example.rework.MonthlyAgenda.application.dto.MonthlyAgendaRequestDto.ReadMonthlyAgendaRequestDto;
import com.example.rework.MonthlyAgenda.application.dto.MonthlyAgendaRequestDto.UpdateMonthlyAgendaRequestDto;
import com.example.rework.MonthlyAgenda.application.dto.MonthlyAgendaResponseDto;
import com.example.rework.MonthlyAgenda.domain.MonthlyAgenda;
import com.example.rework.MonthlyAgenda.domain.repository.MonthlyAgendaRepository;
import com.example.rework.config.security.SecurityUtils;
import com.example.rework.global.error.NotFoundAccountException;
import com.example.rework.global.error.NotFoundAgendaException;
import com.example.rework.global.error.UnAuthorizedException;
import com.example.rework.member.domain.Member;
import com.example.rework.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MonthlyAgendaServiceImpl implements MonthlyAgendaService {
    private final MonthlyAgendaRepository monthlyAgendaRepository;
    private final MemberRepository memberRepository;

    @Override
    public MonthlyAgendaResponseDto.ReadMonthlyAgendaResponseDto readMonthlyAgenda(ReadMonthlyAgendaRequestDto readMonthlyAgendaRequestDto, SecurityUtils securityUtils) {
        Optional<Member> curMember = memberRepository.findByUserId(securityUtils.getCurrentUserId());
        Long currentUserId = curMember.get().getId();

        LocalDateTime start = YearMonth.of(readMonthlyAgendaRequestDto.getYear(), readMonthlyAgendaRequestDto.getMonth()).atDay(1).atStartOfDay();
        LocalDateTime end = YearMonth.of(readMonthlyAgendaRequestDto.getYear(), readMonthlyAgendaRequestDto.getMonth()).atEndOfMonth().atTime(23, 59, 59);

        Optional<MonthlyAgenda> monthlyAgendaOptional = monthlyAgendaRepository.findByMemberIdAndCreatedAtBetween(currentUserId, start, end);

        if (monthlyAgendaOptional.isPresent()) {
            MonthlyAgenda monthlyAgenda = monthlyAgendaOptional.get();
            return MonthlyAgendaResponseDto.ReadMonthlyAgendaResponseDto.builder()
                    .agendaId(monthlyAgenda.getId())
                    .todo(monthlyAgenda.getTodo())
                    .state(monthlyAgenda.isState())
                    .createTime(monthlyAgenda.getCreatedAt())
                    .build();
        } else {
            return MonthlyAgendaResponseDto.ReadMonthlyAgendaResponseDto.builder().build();
        }


    }

    @Override
    @Transactional
    public MonthlyAgendaResponseDto.CreateMonthlyAgendaResponseDto createMonthlyAgenda(CreateMonthlyAgendaRequestDto createMonthlyAgendaRequestDto, SecurityUtils securityUtils) {
        Optional<Member> curMember = memberRepository.findByUserId(securityUtils.getCurrentUserId());
        Long currentUserId = curMember.get().getId();

        Member member = memberRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundAccountException("Member not found with id: " + currentUserId));

        MonthlyAgenda monthlyAgenda = MonthlyAgenda.builder()
                .member(member)
                .todo(createMonthlyAgendaRequestDto.getTodo())
                .state(false)
                .build();

        monthlyAgendaRepository.save(monthlyAgenda);

        return MonthlyAgendaResponseDto.CreateMonthlyAgendaResponseDto.builder()
                .agendaId(monthlyAgenda.getId())
                .todo(monthlyAgenda.getTodo())
                .state(monthlyAgenda.isState())
                .build();
    }

    @Override
    @Transactional
    public MonthlyAgendaResponseDto.UpdateMonthlyAgendaResponseDto updateMonthlyAgenda(UpdateMonthlyAgendaRequestDto updateMonthlyAgendaRequestDto, SecurityUtils securityUtils) {
        Optional<Member> curMember = memberRepository.findByUserId(securityUtils.getCurrentUserId());
        Long currentUserId = curMember.get().getId();

        Member member = memberRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundAccountException("Member not found with id: " + currentUserId));
        MonthlyAgenda monthlyAgenda = monthlyAgendaRepository.findById(updateMonthlyAgendaRequestDto.getAgendaId())
                .orElseThrow(() -> new NotFoundAgendaException("Goal not found with id: " + updateMonthlyAgendaRequestDto.getAgendaId()));

        if (!monthlyAgenda.getMember().getId().equals(currentUserId)) {
            throw new UnAuthorizedException("유저가 소유한 아젠다가 아닙니다.");
        }
        monthlyAgenda.setTodo(updateMonthlyAgendaRequestDto.getTodo());

        return MonthlyAgendaResponseDto.UpdateMonthlyAgendaResponseDto
                .builder()
                .agendaId(monthlyAgenda.getId())
                .todo(monthlyAgenda.getTodo())
                .state(monthlyAgenda.isState())
                .build();
    }

    @Override
    @Transactional
    public boolean deleteMonthlyAgenda(Long monthlyAgendaId, SecurityUtils securityUtils) {
        Optional<Member> curMember = memberRepository.findByUserId(securityUtils.getCurrentUserId());
        Long currentUserId = curMember.get().getId();

        Member member = memberRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundAccountException("Member not found with id: " + currentUserId));
        MonthlyAgenda monthlyAgenda = monthlyAgendaRepository.findById(monthlyAgendaId)
                .orElseThrow(() -> new NotFoundAgendaException("Goal not found with id: " + monthlyAgendaId));

        if (!monthlyAgenda.getMember().getId().equals(currentUserId)) {
            throw new UnAuthorizedException("유저가 소유한 아젠다가 아닙니다.");
        }
        monthlyAgendaRepository.deleteById(monthlyAgenda.getId());

        return true;
    }

}
