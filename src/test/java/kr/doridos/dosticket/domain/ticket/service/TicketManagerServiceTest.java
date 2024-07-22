package kr.doridos.dosticket.domain.ticket.service;

import kr.doridos.dosticket.domain.category.entity.Category;
import kr.doridos.dosticket.domain.category.exception.CategoryNotFoundException;
import kr.doridos.dosticket.domain.category.repository.CategoryRepository;
import kr.doridos.dosticket.domain.place.entity.Place;
import kr.doridos.dosticket.domain.place.repository.PlaceRepository;
import kr.doridos.dosticket.domain.ticket.dto.TicketCreateRequest;
import kr.doridos.dosticket.domain.ticket.dto.TicketUpdateRequest;
import kr.doridos.dosticket.domain.ticket.entity.Ticket;
import kr.doridos.dosticket.domain.ticket.exception.OpenDateNotCorrectException;
import kr.doridos.dosticket.domain.ticket.exception.PlaceNotFoundException;
import kr.doridos.dosticket.domain.ticket.exception.UserNotTicketManagerException;
import kr.doridos.dosticket.domain.ticket.repository.TicketRepository;
import kr.doridos.dosticket.domain.ticket.fixture.TicketFixture;
import kr.doridos.dosticket.domain.user.fixture.UserFixture;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TicketManagerServiceTest {

    @InjectMocks
    private TicketManagerService ticketManagerService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private CategoryRepository categoryRepository;


    Category category = new Category(1L, "뮤지컬");
    Place place = new Place(1L, "세종문화회관", "서울", LocalDateTime.now(), LocalDateTime.now());

    @DisplayName("티켓 생성을 진행한다")
    @Nested
    class CreateTicket {
        @Test
        void 티켓_생성에_성공한다() {
            TicketCreateRequest ticketCreateRequest = TicketFixture.티켓_생성_요청();
            Ticket createTicket = TicketFixture.티켓_생성();

            given(ticketRepository.save(any(Ticket.class))).willReturn(createTicket);
            given(categoryRepository.findById(ticketCreateRequest.getCategoryId())).willReturn(Optional.of(category));
            given(placeRepository.findById(ticketCreateRequest.getPlaceId())).willReturn(Optional.of(place));

            ticketManagerService.createTicket(ticketCreateRequest, UserFixture.관리자_생성());

            then(ticketRepository).should().save(any(Ticket.class));
        }

        @Test
        void 티켓_생성시_티켓매니저가_아니면_생성에_실패한다() {
            assertThatThrownBy(() -> ticketManagerService.createTicket(TicketFixture.티켓_생성_요청(), UserFixture.일반_유저_생성()))
                    .isInstanceOf(UserNotTicketManagerException.class)
                    .hasMessage("권한이 없는 사용자입니다.");
        }

        @Test
        void 티켓_생성시_카테고리가_존재하지_않으면_예외가_발생한다() {
            given(categoryRepository.findById(TicketFixture.티켓_생성_요청().getCategoryId())).willReturn(Optional.empty());

            assertThatThrownBy(() -> ticketManagerService.createTicket(TicketFixture.티켓_생성_요청(), UserFixture.관리자_생성()))
                    .isInstanceOf(CategoryNotFoundException.class)
                    .hasMessage("카테고리가 존재하지 않습니다.");

            then(categoryRepository).should().findById(TicketFixture.티켓_생성_요청().getCategoryId());
        }

        @Test
        void 티켓_생성시_장소가_존재하지_않으면_예외가_발생한다() {
            given(categoryRepository.findById(TicketFixture.티켓_생성_요청().getCategoryId())).willReturn(Optional.of(category));
            given(placeRepository.findById(TicketFixture.티켓_생성_요청().getPlaceId())).willReturn(Optional.empty());

            assertThatThrownBy(() -> ticketManagerService.createTicket(TicketFixture.티켓_생성_요청(), UserFixture.관리자_생성()))
                    .isInstanceOf(PlaceNotFoundException.class)
                    .hasMessage("장소가 존재하지 않습니다.");

            then(placeRepository).should().findById(TicketFixture.티켓_생성_요청().getPlaceId());
        }

        @Test
        void 티켓_생성시_예약시작시간이_예약마감시간보다_이전이면_예외가_발생한다() {
            LocalDateTime startTime = LocalDateTime.of(2023, 7, 22, 12, 0);
            LocalDateTime endTime = LocalDateTime.of(2023, 7, 21, 12, 0);

            TicketCreateRequest ticketCreateRequest = TicketCreateRequest.builder()
                    .title("모차르트")
                    .content("모차르트 최고의 연주")
                    .runningTime("120분")
                    .openDate(startTime)
                    .endDate(endTime)
                    .startDate(LocalDateTime.of(2023, 7, 23, 13, 0))
                    .placeId(1L)
                    .categoryId(1L).build();

            assertThatThrownBy(() -> ticketManagerService.createTicket(ticketCreateRequest, UserFixture.관리자_생성()))
                    .isInstanceOf(OpenDateNotCorrectException.class)
                    .hasMessage("시작일은 종료일 이후가 될 수 없습니다.");
        }
    }

    @DisplayName("티켓을 수정한다")
    @Nested
    class UpdateTicket {
        @Test
        void 티켓_수정에_성공한다() {
            Ticket ticket = TicketFixture.티켓_생성();
            TicketUpdateRequest ticketUpdateRequest = TicketFixture.티켓_수정_요청();

            given(ticketRepository.findById(1L)).willReturn(Optional.of(ticket));

            ticketManagerService.updateTicket(1L, ticketUpdateRequest, UserFixture.관리자_생성());

            assertSoftly(softly -> {
                softly.assertThat(ticketUpdateRequest.getTitle()).isEqualTo(ticket.getTitle());
                softly.assertThat(ticketUpdateRequest.getContent()).isEqualTo(ticket.getContent());
                softly.assertThat(ticketUpdateRequest.getOpenDate()).isEqualTo(ticket.getOpenDate());
                softly.assertThat(ticketUpdateRequest.getEndDate()).isEqualTo(ticket.getEndDate());
            });
        }

        @Test
        void 티켓_수정시_예약시작시간이_예약마감시간보다_이전이면_예외가_발생한다() {
            Ticket ticket = TicketFixture.티켓_생성();
            LocalDateTime startTime = LocalDateTime.of(2023, 7, 22, 12, 0);
            LocalDateTime endTime = LocalDateTime.of(2023, 7, 21, 12, 0);

            TicketUpdateRequest ticketUpdateRequest = TicketUpdateRequest.builder()
                    .title("호차르트")
                    .content("호차르트 최고의 연주")
                    .runningTime("120분")
                    .openDate(startTime)
                    .endDate(endTime)
                    .startDate(LocalDateTime.of(2023, 7, 23, 13, 0))
                    .build();

            given(ticketRepository.findById(1L)).willReturn(Optional.of(ticket));

            assertThatThrownBy(() -> ticketManagerService.updateTicket(1L, ticketUpdateRequest, UserFixture.관리자_생성()))
                    .isInstanceOf(OpenDateNotCorrectException.class)
                    .hasMessage("시작일은 종료일 이후가 될 수 없습니다.");
        }
    }
}