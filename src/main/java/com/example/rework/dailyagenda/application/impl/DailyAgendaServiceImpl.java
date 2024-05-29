package com.example.rework.dailyagenda.application.impl;

import com.example.rework.config.security.SecurityUtils;
import com.example.rework.dailyagenda.application.DailyAgendaService;
import com.example.rework.dailyagenda.application.dto.DailyAgendaRequestDto.*;
import com.example.rework.dailyagenda.application.dto.DailyAgendaResponseDto.*;
import com.example.rework.dailyagenda.domain.DailyAgenda;
import com.example.rework.dailyagenda.domain.repository.DailyAgendaRepository;
import com.example.rework.global.error.AlreadyPagingIdException;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
                .map(agenda -> new ReadDetailDailyAgendaResponseDto(agenda.getId(), agenda.getTodo(), agenda.isState(),agenda.getPagingId())).toList();

        return new ReadDailyAgendaResponseDto(result);
    }

    @Override
    @Transactional
    public CreateDailyAgendaResponseDto createDailyAgenda(CreateDailyAgendaRequestDto createDailyAgendaRequestDto, SecurityUtils securityUtils) {
        Optional<Member> curMember = memberRepository.findByUserId(securityUtils.getCurrentUserId());
        Long currentUserId = curMember.get().getId();

        LocalDateTime startOfDay = createDailyAgendaRequestDto.getCreatedAt().atStartOfDay();
        LocalDateTime endOfDay = createDailyAgendaRequestDto.getCreatedAt().atTime(LocalTime.MAX);

        if(dailyAgendaRepository.existsByPagingIdAndCreatedAtBetween(createDailyAgendaRequestDto.getPagingId(),startOfDay,endOfDay)){
            throw new AlreadyPagingIdException("이미 등록된 페이징번호입니다");
        }


        Member member = memberRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundAccountException("Member not found with id: " + currentUserId));


        DailyAgenda dailyAgenda = DailyAgenda.builder()
                .member(member)
                .todo(createDailyAgendaRequestDto.getTodo())
                .pagingId(createDailyAgendaRequestDto.getPagingId())
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

    @Override
    public ReadDailyCompleteRateResponseDto readDailyCompleteRate(ReadDailyCompleteRateRequestDto readDailyCompleteRateRequestDto, SecurityUtils securityUtils) {
        Optional<Member> curMember = memberRepository.findByUserId(securityUtils.getCurrentUserId());
        Long currentUserId = curMember.get().getId();

        LocalDate date = LocalDate.of(readDailyCompleteRateRequestDto.getYear(), readDailyCompleteRateRequestDto.getMonth(), readDailyCompleteRateRequestDto.getDay());
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<DailyAgenda> agendaList = dailyAgendaRepository.findByMemberIdAndCreatedAtBetween(currentUserId, start, end);
        int length = agendaList.size();
        int count = 0;
        for (DailyAgenda agenda : agendaList) {
            if (agenda.isState()) {
                count++;
            }
        }
        return ReadDailyCompleteRateResponseDto.builder()
                .completeCount(count)
                .allCount(length)
                .build();

    }

    @Override
    public ReadMonthlyCompleteRateResponseDto readMonthlyCompleteRate(ReadMonthlyCompleteRateRequestDto readMonthlyCompleteRateRequestDto, SecurityUtils securityUtils) {
        System.out.println("test123");
        Optional<Member> curMember = memberRepository.findByUserId(securityUtils.getCurrentUserId());
        Long currentUserId = curMember.get().getId();

        LocalDate startDate = LocalDate.of(readMonthlyCompleteRateRequestDto.getYear(), readMonthlyCompleteRateRequestDto.getMonth(), 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        List<DailyAgenda> agendaList = dailyAgendaRepository.findByMemberIdAndCreatedAtBetween(currentUserId, start, end);
        int length = agendaList.size();
        int count = 0;
        for (DailyAgenda agenda : agendaList) {
            if (agenda.isState()) {
                count++;
            }
        }
        return ReadMonthlyCompleteRateResponseDto.builder()
                .completeCount(count)
                .allCount(length)
                .build();
    }

    @Override
    @Transactional
    public List<ReadDetailDailyAgendaResponseDto> bulkUpdateDailyAgendaByPagingId(List<UpdateDailyAgendaListRequestDto> updateDailyAgendaListRequestDtoList, SecurityUtils securityUtils) {
        Optional<Member> curMember = memberRepository.findByUserId(SecurityUtils.getCurrentUserId());
        Long currentUserId = curMember.get().getId();
        LocalDate createdAt = updateDailyAgendaListRequestDtoList.get(0).getCreatedAt();

        //pagingId 중복확인
        checkDuplicatePagingId(updateDailyAgendaListRequestDtoList);

        // createdAt을 LocalDateTime으로 변환
        LocalDateTime startOfDay = createdAt.atStartOfDay();
        LocalDateTime endOfDay = createdAt.atTime(LocalTime.MAX);

        // DB에서 현재 유저의 아젠다 리스트 조회
        List<DailyAgenda> dailyAgendaList = dailyAgendaRepository.findByMemberIdAndCreatedAtBetween(currentUserId, startOfDay,endOfDay);

        // 현재 유저가 소유한 아젠다가 아닌 경우 예외 처리
        dailyAgendaList.stream().filter(agenda -> !agenda.getMember().getId().equals(currentUserId))
                .forEach(agenda -> {
                    throw new UnAuthorizedException("유저가 소유한 아젠다가 아닙니다.");
                });

        // 아젠다 아이디로 아젠다를 찾아 pagingId를 업데이트
        for (UpdateDailyAgendaListRequestDto updateDailyAgendaRequestDtos : updateDailyAgendaListRequestDtoList) {
            DailyAgenda dailyAgenda = dailyAgendaList.
                    stream()
                    .filter(agenda -> agenda.getId().equals(updateDailyAgendaRequestDtos.getAgendaId()))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundAgendaException("agenda not found with id:" + updateDailyAgendaRequestDtos.getAgendaId()));

            dailyAgenda.updatePagingId(updateDailyAgendaRequestDtos.getPagingId());
        }

        // 업데이트된 아젠다 리스트 저장
        List<DailyAgenda> dailyAgenda = dailyAgendaRepository.saveAll(dailyAgendaList);

        // 업데이트된 아젠다 리스트 반환
        List<ReadDetailDailyAgendaResponseDto> result = dailyAgenda.stream()
                .map(agenda -> new ReadDetailDailyAgendaResponseDto(agenda.getId(), agenda.getTodo(), agenda.isState(),agenda.getPagingId())).toList();

        return result;
    }

    private static void checkDuplicatePagingId(List<UpdateDailyAgendaListRequestDto> updateDailyAgendaListRequestDtoList) {
        Map<Long, List<UpdateDailyAgendaListRequestDto>> groupedByPagingId = updateDailyAgendaListRequestDtoList.stream()
                .collect(Collectors.groupingBy(UpdateDailyAgendaListRequestDto::getPagingId));

        // 중복된 pagingId가 있는지 확인
        boolean hasDuplicates = groupedByPagingId.values().stream().anyMatch(group -> group.size() > 1);

        if (hasDuplicates) {
            throw new AlreadyPagingIdException("pagingId 중복 오류");
        }
    }
}
