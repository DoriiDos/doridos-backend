package kr.doridos.dosticket.domain.ticket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.doridos.dosticket.domain.auth.dto.SignInRequest;
import kr.doridos.dosticket.domain.auth.service.AuthService;
import kr.doridos.dosticket.domain.ticket.dto.TicketCreateRequest;
import kr.doridos.dosticket.domain.ticket.service.TicketManagerService;
import kr.doridos.dosticket.domain.user.UserType;
import kr.doridos.dosticket.domain.user.dto.UserSignUpRequest;
import kr.doridos.dosticket.domain.user.service.UserService;
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
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class TicketManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    TicketManagerService ticketManagerService;

    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    String ticketManagerToken;
    String userToken;

    @BeforeEach
    void saveUser() {
        UserSignUpRequest ticketManagerSignUpRequest = new UserSignUpRequest("test@email.com", "123456a!", "도리도스","01012341234", UserType.TICKET_MANAGER);
        userService.signUp(ticketManagerSignUpRequest);

        UserSignUpRequest userSignUpRequest = new UserSignUpRequest("test2@email.com", "123456a!", "도리도도","01012341234", UserType.USER);
        userService.signUp(userSignUpRequest);

        final SignInRequest signInRequest = new SignInRequest("test@email.com", "123456a!");
        ticketManagerToken = authService.signIn(signInRequest).getToken();

        final SignInRequest signInUserRequest = new SignInRequest("test2@email.com", "123456a!");
        userToken = authService.signIn(signInUserRequest).getToken();
    }

    @Test
    @DisplayName("티켓 생성에 성공한다. - 201")
    public void createTicket_success() throws Exception {
        TicketCreateRequest ticketCreateRequest = TicketCreateRequest.builder()
                .title("모차르트")
                .content("모차르트 최고의 연주")
                .runningTime("120분")
                .openDate(LocalDateTime.of(2023, 7, 22, 12, 0))
                .endDate(LocalDateTime.of(2023, 7, 23, 12, 0))
                .startDate(LocalDateTime.of(2023, 7, 23, 13, 0))
                .placeId(1L)
                .categoryId(1L).build();

        mockMvc.perform(post("/manager/tickets")
                        .header("Authorization", "Bearer " + ticketManagerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketCreateRequest)))
                .andExpect(status().isCreated())
                .andDo(document("ticketCreateFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("티켓 생성시 일반유저라면 예외가 발생한다. - 401")
    public void createTicket_roleUser_throwException401() throws Exception {
        TicketCreateRequest ticketCreateRequest = TicketCreateRequest.builder()
                .title("모차르트")
                .content("모차르트 최고의 연주")
                .runningTime("120분")
                .openDate(LocalDateTime.of(2023, 7, 22, 12, 0))
                .endDate(LocalDateTime.of(2023, 7, 23, 12, 0))
                .startDate(LocalDateTime.of(2023, 7, 23, 13, 0))
                .placeId(1L)
                .categoryId(1L).build();

        mockMvc.perform(post("/manager/tickets")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketCreateRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("티켓등록 권한이 없습니다."))
                .andDo(document("ticketCreate",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("티켓 생성시 카테고리가 존재하지 않으면 예외가 발생한다 - 400")
    public void createTicket_notExistCategory_throwException400() throws Exception {
        TicketCreateRequest ticketCreateRequest = TicketCreateRequest.builder()
                .title("모차르트")
                .content("모차르트 최고의 연주")
                .runningTime("120분")
                .openDate(LocalDateTime.of(2023, 7, 22, 12, 0))
                .endDate(LocalDateTime.of(2023, 7, 23, 12, 0))
                .startDate(LocalDateTime.of(2023, 7, 23, 13, 0))
                .placeId(1L)
                .categoryId(100L).build();

        mockMvc.perform(post("/manager/tickets")
                        .header("Authorization", "Bearer " + ticketManagerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("카테고리가 존재하지 않습니다."));
    }

    @Test
    @DisplayName("티켓 생성시 장소가 존재하지 않으면 예외가 발생한다 - 400")
    public void createTicket_notExistPlace_throwException400() throws Exception {
        TicketCreateRequest ticketCreateRequest = TicketCreateRequest.builder()
                .title("모차르트")
                .content("모차르트 최고의 연주")
                .runningTime("120분")
                .openDate(LocalDateTime.of(2023, 7, 22, 12, 0))
                .endDate(LocalDateTime.of(2023, 7, 23, 12, 0))
                .startDate(LocalDateTime.of(2023, 7, 23, 13, 0))
                .placeId(100L)
                .categoryId(1L).build();

        mockMvc.perform(post("/manager/tickets")
                        .header("Authorization", "Bearer " + ticketManagerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("장소가 존재하지 않습니다."));
    }

    @Test
    @DisplayName("티켓 생성시 예미시작시간과 마감시간이 유효하지 않으면 예외가 발생한다 - 400")
    public void createTicket_notCorrectOpenDate_throwException400() throws Exception {
        TicketCreateRequest ticketCreateRequest = TicketCreateRequest.builder()
                .title("모차르트")
                .content("모차르트 최고의 연주")
                .runningTime("120분")
                .openDate(LocalDateTime.of(2023, 7, 22, 12, 0))
                .endDate(LocalDateTime.of(2023, 7, 21, 12, 0))
                .startDate(LocalDateTime.of(2023, 7, 23, 13, 0))
                .placeId(100L)
                .categoryId(1L).build();

        mockMvc.perform(post("/manager/tickets")
                        .header("Authorization", "Bearer " + ticketManagerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("예매 시작일은 종료일 이후가 될 수 없습니다."));
    }
}