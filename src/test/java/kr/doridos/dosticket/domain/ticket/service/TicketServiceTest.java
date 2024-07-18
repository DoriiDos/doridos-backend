package kr.doridos.dosticket.domain.ticket.service;

import kr.doridos.dosticket.domain.ticket.dto.TicketInfoResponse;
import kr.doridos.dosticket.domain.ticket.entity.Ticket;
import kr.doridos.dosticket.domain.ticket.fixture.TicketFixture;
import kr.doridos.dosticket.domain.ticket.repository.TicketRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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
    }
}
