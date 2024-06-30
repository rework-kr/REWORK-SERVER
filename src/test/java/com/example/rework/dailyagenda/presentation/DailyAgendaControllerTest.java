package com.example.rework.dailyagenda.presentation;

import com.example.rework.auth.MemberRole;
import com.example.rework.dailyagenda.application.dto.DailyAgendaRequestDto;
import com.example.rework.dailyagenda.domain.DailyAgenda;
import com.example.rework.dailyagenda.fixture.DailyAgendaFixture;
import com.example.rework.member.domain.Member;
import com.example.rework.util.ControllerTestSupport;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DailyAgendaControllerTest extends ControllerTestSupport {
    private static Long dailyAgendaId;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .name("김민우1234")
                .password("test1234")
                .userId("kbsserver@naver.com")
                .role(MemberRole.MEMBER)
                .state(true)
                .build();
        DailyAgenda dailyAgenda = DailyAgenda.builder()
                .todo("오늘의 아젠다")
                .state(false)
                .pagingId(1L)
                .member(member)
                .build();

        memberRepository.saveAndFlush(member);
        DailyAgenda savedAgenda = dailyAgendaRepository.saveAndFlush(dailyAgenda);
        dailyAgendaId = savedAgenda.getId();
    }

    @DisplayName("회원은 오늘의 아젠다를 생성할 수 있다.")
    @Test
    @WithMockUser(username = "kbsserver@naver.com", authorities = {"MEMBER"})
    void createAgenda() throws Exception {
        //given
        String url = "/api/v1/dailyAgenda";
        DailyAgendaRequestDto.CreateDailyAgendaRequestDto createDailyAgendaRequestDto = DailyAgendaFixture.createAgenda();

        //when
        MvcResult mvcResult = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDailyAgendaRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        //then
        mvcResult.getResponse().setContentType("application/json;charset=UTF-8");
        String result = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(result);
        JsonNode resultData = jsonNode.get("data");
        Long id = resultData.get("agendaId").asLong();
        String todo = resultData.get("todo").asText();

        assertAll(
                () -> assertThat(id).isNotNull(),
                () -> assertThat(todo).isEqualTo("생성된 오늘의 아젠다")
        );
    }

    @DisplayName("회원은 오늘의 아젠다를 조회할 수 있다.")
    @Test
    @WithMockUser(username = "kbsserver@naver.com", authorities = {"MEMBER"})
    void readAgenda() throws Exception {
        //given
        String url = "/api/v1/dailyAgenda";
        LocalDate Today = LocalDate.now();
        int year = Today.getYear();
        int month = Today.getMonthValue();
        int day = Today.getDayOfMonth();
        boolean inputState = false;

        //when
        MvcResult mvcResult = mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("year", String.valueOf(year))
                        .param("month", String.valueOf(month))
                        .param("day", String.valueOf(day))
                        .param("state", String.valueOf(inputState))
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        mvcResult.getResponse().setContentType("application/json;charset=UTF-8");
        String result = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(result);
        JsonNode resultData = jsonNode.get("data");
        JsonNode resultList = resultData.get("agendaList");

        assertThat(resultList.isArray()).isTrue();
        JsonNode firstAgenda = resultList.get(0);

        Long agendaId = firstAgenda.get("agendaId").asLong();
        String todo = firstAgenda.get("todo").asText();
        boolean state = firstAgenda.get("state").asBoolean();

        assertAll(
                () -> assertThat(agendaId).isNotNull(),
                () -> assertThat(todo).isEqualTo("오늘의 아젠다"),
                () -> assertThat(state).isFalse()
        );
    }

    @DisplayName("회원은 오늘의 아젠다를 수정할 수 있다.")
    @Test
    @WithMockUser(username = "kbsserver@naver.com", authorities = {"MEMBER"})
    void updateAgenda() throws Exception {
        //given
        String url = "/api/v1/dailyAgenda";

        DailyAgendaRequestDto.UpdateDailyAgendaRequestDto updateDailyAgendaRequestDto = DailyAgendaFixture.updateAgenda(dailyAgendaId);

        MvcResult mvcResult = mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDailyAgendaRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        //then
        mvcResult.getResponse().setContentType("application/json;charset=UTF-8");
        String result = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(result);
        JsonNode resultData = jsonNode.get("data");
        Long id = resultData.get("agendaId").asLong();
        String todo = resultData.get("todo").asText();
        boolean state = resultData.get("state").asBoolean();

        assertAll(
                () -> assertThat(id).isNotNull(),
                () -> assertThat(todo).isEqualTo("수정한 아젠다"),
                () -> assertThat(state).isTrue()
        );
    }

    @DisplayName("회원은 오늘의 아젠다를 삭제할 수 있다.")
    @Test
    @WithMockUser(username = "kbsserver@naver.com", authorities = {"MEMBER"})
    void deleteAgenda() throws Exception {
        //given
        String url = "/api/v1/dailyAgenda?dailyAgendaId=" + dailyAgendaId;

        //when
        MvcResult mvcResult = mockMvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        mvcResult.getResponse().setContentType("application/json;charset=UTF-8");
        String result = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(result);
        JsonNode resultData = jsonNode.get("data");
        String dataResult = resultData.asText();

        assertAll(
                () -> assertThat(dataResult).isEqualTo("true")
        );
    }

    @DisplayName("하루의 아젠다 수행률 확인")
    @Test
    @WithMockUser(username = "kbsserver@naver.com", authorities = {"MEMBER"})
    void readDailyCompleteRate() throws Exception {
        //given
        String url = "/api/v1/dailyAgenda/dailyCompleteRate";
        LocalDate Today = LocalDate.now();
        int year = Today.getYear();
        int month = Today.getMonthValue();
        int day = Today.getDayOfMonth();

        //when
        MvcResult mvcResult = mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("year", String.valueOf(year))
                        .param("month", String.valueOf(month))
                        .param("day", String.valueOf(day))
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        mvcResult.getResponse().setContentType("application/json;charset=UTF-8");
        String result = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(result);
        JsonNode resultData = jsonNode.get("data");

        Long completeCount = resultData.get("completeCount").asLong();
        Long allCount = resultData.get("allCount").asLong();

        assertAll(
                () -> assertThat(completeCount).isEqualTo(0),
                () -> assertThat(allCount).isEqualTo(1)
        );
    }

    @DisplayName("한달의 아젠다 수행률 확인")
    @Test
    @WithMockUser(username = "kbsserver@naver.com", authorities = {"MEMBER"})
    void readMonthlyCompleteRate() throws Exception {
        //given
        String url = "/api/v1/dailyAgenda/monthlyCompleteRate";
        LocalDate Today = LocalDate.now();
        int year = Today.getYear();
        int month = Today.getMonthValue();

        //when
        MvcResult mvcResult = mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("year", String.valueOf(year))
                        .param("month", String.valueOf(month))
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        mvcResult.getResponse().setContentType("application/json;charset=UTF-8");
        String result = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(result);
        JsonNode resultData = jsonNode.get("data");

        Long completeCount = resultData.get("completeCount").asLong();
        Long allCount = resultData.get("allCount").asLong();

        assertAll(
                () -> assertThat(completeCount).isEqualTo(0),
                () -> assertThat(allCount).isEqualTo(1)
        );
    }


    @DisplayName("오늘의 아젠다 pagingId 별 정렬 테스트")
    @Test
    @WithMockUser(username = "kbsserver@naver.com", authorities = {"MEMBER"})
    void updateDailyAgendaPagingIdTest() throws Exception {
        //given
        String url = "/api/v1/dailyAgenda/bulk-update-pagingId";
        Long updatePagingId = 2L;
        List<DailyAgendaRequestDto.UpdateDailyAgendaListRequestDto> updateDailyAgendaListRequestDtos = DailyAgendaFixture.updateDailyAgendaListRequestDtoList(dailyAgendaId, updatePagingId);

        //when
        MvcResult mvcResult = mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDailyAgendaListRequestDtos))
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        mvcResult.getResponse().setContentType("application/json;charset=UTF-8");
        String result = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(result);
        JsonNode resultData = jsonNode.get("data").get(0);
        Long pagingId = resultData.get("pagingId").asLong();
        assertAll(
                () -> assertThat(pagingId).isEqualTo(2L)
        );
    }
}
