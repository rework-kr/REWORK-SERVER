package com.example.rework.MonthlyAgenda.presentation;

import com.example.rework.MonthlyAgenda.restapi.MonthlyAgendaApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/monthlyAgenda")
@RequiredArgsConstructor
@Slf4j
public class MonthlyAgendaController implements MonthlyAgendaApi {
}
