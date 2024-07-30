package kr.doridos.dosticket.domain.ticket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.doridos.dosticket.domain.category.repository.CategoryRepository;
import kr.doridos.dosticket.domain.place.repository.PlaceRepository;
import kr.doridos.dosticket.domain.ticket.fixture.CategoryFixture;
import kr.doridos.dosticket.domain.ticket.fixture.PlaceFixture;
import kr.doridos.dosticket.domain.ticket.fixture.TicketFixture;
import kr.doridos.dosticket.domain.ticket.repository.TicketRepository;
import kr.doridos.dosticket.domain.user.fixture.UserFixture;
import kr.doridos.dosticket.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.save(UserFixture.관리자_생성());
        categoryRepository.save(CategoryFixture.카테고리_생성());
        placeRepository.save(PlaceFixture.장소_생성());
        ticketRepository.save(TicketFixture.티켓_생성());
    }

    @Test
    void 티켓_조회에_성공한다_200() throws Exception {
        Long ticketId = TicketFixture.티켓_생성().getId();

        mockMvc.perform(get("/tickets/" + ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketId)))
                .andExpect(status().isOk())
                .andDo(document("getTicket",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    void 티켓_페이징_조회에_성공한다200() throws Exception {
        mockMvc.perform(get("/tickets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("ticketPaging",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    void 카테고리로_티켓_페이징_조회에_성공한다200() throws Exception {
        Long categoryId = TicketFixture.티켓_생성().getCategory().getId();

        mockMvc.perform(get("/tickets/category/" + categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryId)))
                .andExpect(status().isOk())
                .andDo(document("ticketCategoryPaging",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}
