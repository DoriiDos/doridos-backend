package kr.doridos.dosticket.domain.ticket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.doridos.dosticket.domain.auth.dto.SignInRequest;
import kr.doridos.dosticket.domain.auth.service.AuthService;
import kr.doridos.dosticket.domain.ticket.dto.TicketCreateRequest;
import kr.doridos.dosticket.domain.ticket.dto.TicketUpdateRequest;
import kr.doridos.dosticket.domain.ticket.fixture.TicketFixture;
import kr.doridos.dosticket.domain.ticket.service.TicketManagerService;
import kr.doridos.dosticket.domain.user.entity.User;
import kr.doridos.dosticket.domain.user.exception.UserNotFoundException;
import kr.doridos.dosticket.domain.user.fixture.UserFixture;
import kr.doridos.dosticket.domain.user.repository.UserRepository;
import kr.doridos.dosticket.domain.user.service.UserService;
import kr.doridos.dosticket.exception.ErrorCode;
import org.junit.jupiter.api.*;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
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

    @Autowired
    UserRepository userRepository;

    String userToken;
    String ticketManagerToken;
    Long ticketId;
    Long ticketManagerId;

    @BeforeEach
    void setUp() {
        ticketManagerId = userService.signUp(UserFixture.관리자_생성_요청());
        userService.signUp(UserFixture.일반_생성_요청());
        SignInRequest signInRequest = new SignInRequest(UserFixture.일반_생성_요청().getEmail(), UserFixture.일반_생성_요청().getPassword());
        SignInRequest adminSignInRequest = new SignInRequest(UserFixture.관리자_생성_요청().getEmail(), UserFixture.관리자_생성_요청().getPassword());
        userToken = authService.signIn(signInRequest).getToken();
        ticketManagerToken = authService.signIn(adminSignInRequest).getToken();
        ticketId = saveTicket();
    }

    @Test
    void 티켓생성에_성공한다201() throws Exception {
        TicketCreateRequest ticketCreateRequest = TicketFixture.티켓_생성_요청();

        mockMvc.perform(post("/manager/tickets")
                        .header("Authorization", "Bearer " + ticketManagerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketCreateRequest)))
                .andExpect(status().isCreated())
                .andDo(document("ticketCreate",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("티켓 생성시 일반유저라면 예외가 발생한다. - 401")
    void 티켓_생성시_일반유저라면_예외가_발생한다401() throws Exception {
        TicketCreateRequest ticketCreateRequest = TicketFixture.티켓_생성_요청();

        mockMvc.perform(post("/manager/tickets")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketCreateRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("권한이 없는 사용자입니다."))
                .andDo(document("ticketCreateFail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    void 티켓_생성시_카테고리가_존재하지_않으면_예외가_발생한다401() throws Exception {
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
    void 티켓_생성시_장소가_존재하지_않으면_예외가_발생한다401() throws Exception {
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
    void 티켓수정에_성공한다() throws Exception {
        TicketUpdateRequest ticketUpdateRequest = TicketFixture.티켓_수정_요청();

        mockMvc.perform(patch("/manager/tickets/" + ticketId)
                        .header("Authorization", "Bearer " + ticketManagerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketUpdateRequest)))
                .andExpect(status().isNoContent())
                .andDo(document("ticketUpdate",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    void 티켓수정시_예미시작시간과_마감시간이_유효하지_않으면_예외가_발생한다400() throws Exception {
        TicketUpdateRequest ticketUpdateRequest = TicketUpdateRequest.builder()
                .title("호차르트")
                .content("호호차차르트")
                .runningTime("120분")
                .openDate(LocalDateTime.of(2024, 7, 22, 12, 0))
                .endDate(LocalDateTime.of(2024, 7, 21, 12, 0))
                .startDate(LocalDateTime.of(2024, 7, 23, 13, 0))
                .build();

        mockMvc.perform(patch("/manager/tickets/" + ticketId)
                        .header("Authorization", "Bearer " + ticketManagerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketUpdateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("시작일은 종료일 이후가 될 수 없습니다."));
    }

    @Test
    void 티켓수정시_일반유저라면_예외가_발생한다400() throws Exception {
        TicketUpdateRequest ticketUpdateRequest = TicketUpdateRequest.builder()
                .title("호차르트")
                .content("호호차차르트")
                .runningTime("120분")
                .openDate(LocalDateTime.of(2024, 7, 22, 12, 0))
                .endDate(LocalDateTime.of(2024, 7, 21, 12, 0))
                .startDate(LocalDateTime.of(2024, 7, 23, 13, 0))
                .build();

        mockMvc.perform(patch("/manager/tickets/" + ticketId)
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketUpdateRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("권한이 없는 사용자입니다."));
    }

    private Long saveTicket() {
        TicketCreateRequest ticketCreateRequest = TicketFixture.티켓_생성_요청();

        User user = userRepository.findById(ticketManagerId)
                .orElseThrow(() -> {
                    throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
                });

        return ticketManagerService.createTicket(ticketCreateRequest, user);
    }
}