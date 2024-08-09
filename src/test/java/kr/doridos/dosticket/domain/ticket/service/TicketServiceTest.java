package kr.doridos.dosticket.domain.ticket.service;

import kr.doridos.dosticket.domain.category.entity.Category;
import kr.doridos.dosticket.domain.ticket.dto.TicketInfoResponse;
import kr.doridos.dosticket.domain.ticket.dto.TicketPageResponse;
import kr.doridos.dosticket.domain.ticket.entity.Ticket;
import kr.doridos.dosticket.domain.ticket.exception.TicketNotFoundException;
import kr.doridos.dosticket.domain.ticket.fixture.CategoryFixture;
import kr.doridos.dosticket.domain.ticket.fixture.TicketFixture;
import kr.doridos.dosticket.domain.ticket.repository.TicketRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class TicketServiceTest {

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private TicketRepository ticketRepository;

    private List<Ticket> tickets;
    private Category parentCategory;
    private Category childCategory;
    private List<TicketPageResponse> ticketPageResponse;

    @BeforeEach
    public void setUp() {
        parentCategory = CategoryFixture.카테고리_생성();
        childCategory = CategoryFixture.하위_카테고리_생성();
        tickets = List.of(
                TicketFixture.티켓_생성(),
                TicketFixture.티켓_생성2()
        );
        ticketPageResponse = tickets.stream()
                .map(TicketPageResponse::convertToDto)
                .collect(Collectors.toList());
    }

    @DisplayName("티켓을 조회한다")
    @Nested
    class TicketInfo {

        @Test
        void 티켓_정보_조회에_성공한다() {
            Ticket ticket = TicketFixture.티켓_생성();
            given(ticketRepository.findById(ticket.getId())).willReturn(Optional.of(ticket));

            TicketInfoResponse ticketInfoResponse = ticketService.ticketInfo(ticket.getId());

            assertSoftly(softly -> {
                softly.assertThat(ticketInfoResponse.getTitle()).isEqualTo(ticket.getTitle());
                softly.assertThat(ticketInfoResponse.getContent()).isEqualTo(ticket.getContent());
                softly.assertThat(ticketInfoResponse.getRunningTime()).isEqualTo(ticket.getRunningTime());
                softly.assertThat(ticketInfoResponse.getOpenDate()).isEqualTo(ticket.getOpenDate());
                softly.assertThat(ticketInfoResponse.getEndDate()).isEqualTo(ticket.getEndDate());
                softly.assertThat(ticketInfoResponse.getStartDate()).isEqualTo(ticket.getStartDate());
                softly.assertThat(ticketInfoResponse.getPlace()).isEqualTo(ticket.getPlace().getName());
                softly.assertThat(ticketInfoResponse.getCategoryName()).isEqualTo(ticket.getCategory().getName());
            });
        }

        @Test
        void 티켓이_존재하지_않으면_예외가_발생한다() {
            Ticket ticket = TicketFixture.티켓_생성();
            given(ticketRepository.findById(ticket.getId())).willReturn(Optional.empty());

            assertThatThrownBy(() -> ticketService.ticketInfo(ticket.getId()))
                    .isInstanceOf(TicketNotFoundException.class)
                    .hasMessage("티켓을 찾을 수 없습니다.");
        }

        @Test
        void 티켓을_페이징_조회한다() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Ticket> ticketPage = new PageImpl<>(tickets, pageable, tickets.size());

            given(ticketRepository.findAll(pageable)).willReturn(ticketPage);
            Page<TicketPageResponse> result = ticketService.findAllTickets(pageable);

            assertSoftly(softly -> {
                softly.assertThat(ticketPage.getTotalElements()).isEqualTo(result.getTotalElements());
                softly.assertThat(ticketPage.getTotalPages()).isEqualTo(result.getTotalPages());
            });
        }


        @Test
        void 부모카테고리로_티켓을_조회한다() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<TicketPageResponse> ticketPage = new PageImpl<>(ticketPageResponse, pageable, tickets.size());

            given(ticketRepository.findTicketsByCategoryId(parentCategory.getId(), pageable)).willReturn(ticketPage);

            Page<TicketPageResponse> result = ticketService.findTicketsByCategoryId(parentCategory.getId(), pageable);

            assertSoftly(softly -> {
                softly.assertThat(result.getTotalElements()).isEqualTo(2);
                softly.assertThat(result.getContent()).extracting("id").containsExactlyInAnyOrder(tickets.get(0).getId(), tickets.get(1).getId());
            });
        }

        @Test
        void 자식_카테고리로_티켓을_조회한다() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<TicketPageResponse> ticketPage = new PageImpl<>(List.of(ticketPageResponse.get(1)), pageable, tickets.size());

            given(ticketRepository.findTicketsByCategoryId(childCategory.getId(), pageable)).willReturn(ticketPage);

            Page<TicketPageResponse> result = ticketService.findTicketsByCategoryId(childCategory.getId(), pageable);

            assertSoftly(softly -> {
                softly.assertThat(result.getTotalElements()).isEqualTo(1);
                softly.assertThat(result.getContent().get(0).getId()).isEqualTo(tickets.get(1).getId());
            });
        }

        @Test
        void 해당_기간에_존재하는_티켓을_조회한다() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<TicketPageResponse> ticketPage = new PageImpl<>(ticketPageResponse, pageable, tickets.size());
            LocalDate startDate = LocalDate.of(2000, 9, 10);
            LocalDate endDate = LocalDate.of(2100, 9, 10);

            given(ticketRepository.findTicketsByStartDateBetween(startDate, endDate, pageable)).willReturn(ticketPage);

            Page<TicketPageResponse> result = ticketService.findTicketsByDate(startDate, endDate, pageable);

            assertSoftly(softly -> {
                softly.assertThat(result.getTotalElements()).isEqualTo(2);
                softly.assertThat(ticketPage.getTotalPages()).isEqualTo(result.getTotalPages());
            });
        }

        @Test
        void 해당_기간에_존재하는_티켓이_없는경우를_테스트한다() {
            Pageable pageable = PageRequest.of(0, 10);
            LocalDate startDate = LocalDate.of(2000, 9, 10);
            LocalDate endDate = LocalDate.of(2000, 9, 10);

            given(ticketRepository.findTicketsByStartDateBetween(startDate, endDate, pageable)).willReturn(Page.empty(pageable));

            Page<TicketPageResponse> result = ticketService.findTicketsByDate(startDate, endDate, pageable);

            assertSoftly(softly -> {
                softly.assertThat(result.getTotalElements()).isEqualTo(0);
                softly.assertThat(result.getTotalPages()).isEqualTo(0);
                softly.assertThat(result.getContent()).isEmpty();
            });
        }
    }
}
