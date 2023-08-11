package kr.doridos.dosticket.domain.schedule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.doridos.dosticket.domain.auth.dto.SignInRequest;
import kr.doridos.dosticket.domain.auth.service.AuthService;
import kr.doridos.dosticket.domain.schedule.dto.ScheduleCreateRequest;
import kr.doridos.dosticket.domain.schedule.entity.Schedule;
import kr.doridos.dosticket.domain.schedule.repository.ScheduleRepository;
import kr.doridos.dosticket.domain.schedule.service.ScheduleService;
import kr.doridos.dosticket.domain.ticket.dto.TicketCreateRequest;
import kr.doridos.dosticket.domain.ticket.entity.Ticket;
import kr.doridos.dosticket.domain.ticket.exception.TicketNotFoundException;
import kr.doridos.dosticket.domain.ticket.repository.TicketRepository;
import kr.doridos.dosticket.domain.ticket.service.TicketManagerService;
import kr.doridos.dosticket.domain.user.User;
import kr.doridos.dosticket.domain.user.UserType;
import kr.doridos.dosticket.domain.user.dto.UserSignUpRequest;
import kr.doridos.dosticket.domain.user.exception.UserNotFoundException;
import kr.doridos.dosticket.domain.user.repository.UserRepository;
import kr.doridos.dosticket.domain.user.service.UserService;
import kr.doridos.dosticket.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
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
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private TicketManagerService ticketManagerService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TicketRepository ticketRepository;

    Long ticketManagerId;
    String ticketManagerToken;
    Long ticketId;

    @BeforeEach
    void setUpTestData() {
        UserSignUpRequest ticketManagerSignUpRequest = new UserSignUpRequest("test@email.com", "123456a!", "도리도스", "01012341234", UserType.TICKET_MANAGER);
        ticketManagerId = userService.signUp(ticketManagerSignUpRequest);

        final SignInRequest signInRequest = new SignInRequest("test@email.com", "123456a!");
        ticketManagerToken = authService.signIn(signInRequest).getToken();

        ticketId = saveTicket();
    }

    @Test
    @DisplayName("스케줄 생성에 성공한다 -201")
    public void createSchedule_success() throws Exception {
        ScheduleCreateRequest scheduleCreateRequest = ScheduleCreateRequest.builder()
                .ticketId(ticketId)
                .startTime(LocalDateTime.of(2023, 7, 22, 7, 0))
                .endTime(LocalDateTime.of(2023, 7, 22, 9, 0))
                .build();

        mockMvc.perform(post("/schedules")
                        .header("Authorization", "Bearer " + ticketManagerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleCreateRequest)))
                .andExpect(status().isCreated())
                .andDo(document("scheduleCreate",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("스케줄 생성시 티켓이 존재하지 않으면 예외가 발생한다. -400")
    public void createSchedule_notExistTicket_throwException400() throws Exception {
        ScheduleCreateRequest scheduleCreateRequest = ScheduleCreateRequest.builder()
                .ticketId(0L)
                .startTime(LocalDateTime.of(2023, 7, 22, 7, 0))
                .endTime(LocalDateTime.of(2023, 7, 22, 9, 0))
                .build();

        mockMvc.perform(post("/schedules")
                        .header("Authorization", "Bearer " + ticketManagerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("티켓을 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("스케줄 생성시 시작시간과 끝 시간이 유효하지 않으면 예외가 발생한다.-400")
    public void createSchedule_notCorrectOpenDate_throwException400() throws Exception {
        ScheduleCreateRequest scheduleCreateRequest = ScheduleCreateRequest.builder()
                .ticketId(ticketId)
                .startTime(LocalDateTime.of(2023, 7, 22, 10, 0))
                .endTime(LocalDateTime.of(2023, 7, 22, 9, 0))
                .build();

        mockMvc.perform(post("/schedules")
                        .header("Authorization", "Bearer " + ticketManagerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("시작일은 종료일 이후가 될 수 없습니다."));
    }

    @Test
    @DisplayName("스케줄 생성시 겹치는 스케줄 시간이 존재하면 예외가 발생한다.-400")
    public void createSchedule_duplicatedTime_throwException400() throws Exception {
        saveSchedule();

        ScheduleCreateRequest scheduleCreateRequest = ScheduleCreateRequest.builder()
                .ticketId(ticketId)
                .startTime(LocalDateTime.of(2023, 7, 26, 12, 0))
                .endTime(LocalDateTime.of(2023, 7, 26, 13, 0))
                .build();

        mockMvc.perform(post("/schedules")
                        .header("Authorization", "Bearer " + ticketManagerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("해당시간에 이미 스케줄이 존재합니다."));
    }

    private Long saveTicket() {
        TicketCreateRequest ticketCreateRequest = TicketCreateRequest.builder()
                .title("고차르트").content("고차르트 최고의 연주").runningTime("120분")
                .openDate(LocalDateTime.of(2023, 7, 22, 12, 0))
                .endDate(LocalDateTime.of(2023, 7, 23, 12, 0))
                .startDate(LocalDateTime.of(2023, 7, 26, 13, 0))
                .placeId(1L).categoryId(1L).build();

        User user = userRepository.findById(ticketManagerId)
                .orElseThrow(() -> {
                    throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
                });

        return ticketManagerService.createTicket(ticketCreateRequest, user);
    }

    private void saveSchedule() {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> {
                    throw new TicketNotFoundException(ErrorCode.TICKET_NOT_FOUND);
                });

        Schedule schedule = new Schedule(LocalDateTime.of(2023, 7, 26, 13, 0), LocalDateTime.of(2023, 7, 26, 14, 0), ticket);
        scheduleRepository.save(schedule);
    }
}