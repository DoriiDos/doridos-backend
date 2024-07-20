package kr.doridos.dosticket.domain.ticket.service;

import kr.doridos.dosticket.domain.ticket.dto.TicketInfoResponse;
import kr.doridos.dosticket.domain.ticket.dto.TicketPageResponse;
import kr.doridos.dosticket.domain.ticket.entity.Ticket;
import kr.doridos.dosticket.domain.ticket.exception.TicketNotFoundException;
import kr.doridos.dosticket.domain.ticket.fixture.TicketFixture;
import kr.doridos.dosticket.domain.ticket.repository.TicketRepository;
import kr.doridos.dosticket.domain.user.exception.NicknameAlreadyExistsException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class TicketServiceTest {

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private TicketRepository ticketRepository;

    private Ticket ticket;
    private TicketPageResponse ticketPageResponse;
    private Page<Ticket> ticketPage;
    private Page<TicketPageResponse> pageResponse;

    @BeforeEach
    public void setUp() {
        ticket = TicketFixture.티켓_생성();
        ticketPageResponse = TicketPageResponse.convertToDto(ticket);
        ticketPage = new PageImpl<>(Collections.singletonList(ticket));
        pageResponse = new PageImpl<>(Collections.singletonList(ticketPageResponse));
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
            given(ticketRepository.findById(ticket.getId())).willReturn(Optional.empty());

            assertThatThrownBy(() -> ticketService.ticketInfo(ticket.getId()))
                    .isInstanceOf(TicketNotFoundException.class)
                    .hasMessage("티켓을 찾을 수 없습니다.");
        }

        @Test
        void 티켓을_페이징_조회한다() {
            given(ticketRepository.findAll(any(Pageable.class))).willReturn(ticketPage);

            Pageable pageable = PageRequest.of(0, 10);
            Page<TicketPageResponse> result = ticketService.findAllTickets(pageable);

            assertSoftly(softly -> {
                softly.assertThat(pageResponse.getTotalElements()).isEqualTo(result.getTotalElements());
                softly.assertThat(pageResponse.getTotalPages()).isEqualTo(result.getTotalPages());
            });
        }
    }
}
