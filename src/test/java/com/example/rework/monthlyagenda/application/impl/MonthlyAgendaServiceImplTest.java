package com.example.rework.monthlyagenda.application.impl;

import com.example.rework.MonthlyAgenda.application.dto.MonthlyAgendaRequestDto;
import com.example.rework.MonthlyAgenda.application.impl.MonthlyAgendaServiceImpl;
import com.example.rework.MonthlyAgenda.domain.repository.MonthlyAgendaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MonthlyAgendaServiceImplTest {
    @InjectMocks
    private MonthlyAgendaServiceImpl monthlyAgendaService;
    @Mock
    private MonthlyAgendaRepository monthlyAgendaRepository;

    @DisplayName("이번달 아젠다 생성하게 되면 해당 정보를 반환한다.")
    @Test
    void createMonthlyAgenda() {
        //given
        MonthlyAgendaRequestDto.CreateMonthlyAgendaRequestDto createMonthlyAgendaRequestDto = getMonthlyAgendaReq();
        

        //when
        //then

    }

    @DisplayName("이번달 아젠다를 조회할 수 있다.")
    @Test
    void readMonthlyAgenda() {
        //given

        //when
        //then
    }

    @DisplayName("이번달 아젠다를 수정할 수 있다.")
    @Test
    void updateMonthlyAgenda() {
        //given

        //when
        //then

    }

    @DisplayName("이번달 아젠다를 삭제할 수 있다.")
    @Test
    void deleteMonthlyAgenda() {
        //given

        //when
        //then

    }

    private MonthlyAgendaRequestDto.CreateMonthlyAgendaRequestDto getMonthlyAgendaReq() {
        return MonthlyAgendaRequestDto.CreateMonthlyAgendaRequestDto.builder()
                .todo("이번달 아젠다")
                .build();
    }
}
