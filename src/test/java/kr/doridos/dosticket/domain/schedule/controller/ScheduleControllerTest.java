package kr.doridos.dosticket.domain.schedule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.doridos.dosticket.domain.auth.support.jwt.JwtProvider;
import kr.doridos.dosticket.domain.schedule.dto.ScheduleCreateRequest;
import kr.doridos.dosticket.domain.schedule.entity.Schedule;
import kr.doridos.dosticket.domain.schedule.fixture.ScheduleFixture;
import kr.doridos.dosticket.domain.schedule.fixture.ScheduleSeatFixture;
import kr.doridos.dosticket.domain.schedule.repository.ScheduleRepository;
import kr.doridos.dosticket.domain.schedule.repository.ScheduleSeatRepository;
import kr.doridos.dosticket.domain.schedule.service.ScheduleService;
import kr.doridos.dosticket.domain.ticket.entity.Ticket;
import kr.doridos.dosticket.domain.ticket.fixture.TicketFixture;
import kr.doridos.dosticket.domain.ticket.repository.TicketRepository;
import kr.doridos.dosticket.domain.ticket.service.TicketManagerService;
import kr.doridos.dosticket.domain.user.UserType;
import kr.doridos.dosticket.domain.user.fixture.UserFixture;
import kr.doridos.dosticket.domain.user.repository.UserRepository;
import kr.doridos.dosticket.domain.user.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDateTime;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ScheduleSeatRepository scheduleSeatRepository;

    @Autowired
    private JwtProvider jwtProvider;

    String token;

    @BeforeEach
    void setUp() {
        userRepository.save(UserFixture.관리자_생성());
        ticketRepository.save(TicketFixture.티켓_생성());
        scheduleRepository.save(ScheduleFixture.스케줄_생성());
        scheduleSeatRepository.save(ScheduleSeatFixture.좌석생성());
        token = jwtProvider.createAccessToken(UserFixture.관리자_생성().getEmail(), UserType.TICKET_MANAGER);
    }

    @Test
    public void 스케줄_생성에_성공한다201() throws Exception {
        ScheduleCreateRequest scheduleCreateRequest = ScheduleCreateRequest.builder()
                .ticketId(TicketFixture.티켓_생성().getId())
                .startTime(LocalDateTime.of(2024, 8, 22, 7, 0))
                .endTime(LocalDateTime.of(2024, 8, 22, 9, 0))
                .build();

        mockMvc.perform(post("/schedules")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleCreateRequest)))
                .andExpect(status().isCreated())
                .andDo(document("scheduleCreate",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    public void 스케줄_생성시_티켓이_존재하지_않으면_예외가_발생한다400() throws Exception {
        ScheduleCreateRequest scheduleCreateRequest = ScheduleCreateRequest.builder()
                .ticketId(0L)
                .startTime(LocalDateTime.of(2024, 8, 22, 7, 0))
                .endTime(LocalDateTime.of(2024, 8, 22, 9, 0))
                .build();

        mockMvc.perform(post("/schedules")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("티켓을 찾을 수 없습니다."));
    }

    @Test
    public void 스케줄_생성시_시작시간과_끝_시간이_유효하지_않으면_예외가_발생한다400() throws Exception {
        ScheduleCreateRequest scheduleCreateRequest = ScheduleCreateRequest.builder()
                .ticketId(TicketFixture.티켓_생성().getId())
                .startTime(LocalDateTime.of(2024, 8, 22, 10, 0))
                .endTime(LocalDateTime.of(2024, 8, 22, 9, 0))
                .build();

        mockMvc.perform(post("/schedules")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("시작일은 종료일 이후가 될 수 없습니다."));
    }

    @Test
    public void 스케줄_생성시_겹치는_스케줄_시간이_존재하면_예외가_발생한다400() throws Exception {
        ScheduleCreateRequest scheduleCreateRequest = ScheduleCreateRequest.builder()
                .ticketId(TicketFixture.티켓_생성().getId())
                .startTime(ScheduleFixture.스케줄_생성().getStartTime())
                .endTime(ScheduleFixture.스케줄_생성().getEndTime())
                .build();

        mockMvc.perform(post("/schedules")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("해당시간에 이미 스케줄이 존재합니다."));
    }

    @Test
    public void 티켓에_해당하는_스케줄을_조회한다200() throws Exception {
        Long ticketId = TicketFixture.티켓_생성().getId();

        mockMvc.perform(get("/tickets/" + ticketId + "/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketId)))
                .andExpect(status().isOk())
                .andDo(document("findAllSchedule",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    public void 스케줄에_해당하는_좌석조회에_성공한다200() throws Exception {
        Long ticketId = TicketFixture.티켓_생성().getId();
        Long scheduleId = ScheduleFixture.스케줄_생성().getId();

        mockMvc.perform(get("/tickets/{ticketId}/schedules/{scheduleId}/seats", ticketId, scheduleId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("findAllScheduleSeats",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    public void 스케줄좌석_조회시간이_티켓오픈시간_이전이면_예외가_발생한다400() throws Exception {
        Ticket ticket = TicketFixture.티켓_생성3();
        Schedule schedule = ScheduleFixture.스케줄_생성3();
        ticketRepository.save(ticket);
        scheduleRepository.save(schedule);

        // 스케줄 좌석 조회 요청
        mockMvc.perform(get("/tickets/{ticketId}/schedules/{scheduleId}/seats", ticket.getId(), schedule.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("예매가 아직 시작되지 않았습니다."))
                .andDo(document("findAllScheduleSeatsError",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}