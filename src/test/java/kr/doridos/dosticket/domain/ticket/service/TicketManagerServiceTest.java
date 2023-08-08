package kr.doridos.dosticket.domain.ticket.service;

import kr.doridos.dosticket.domain.category.entity.Category;
import kr.doridos.dosticket.domain.category.exception.CategoryNotFoundException;
import kr.doridos.dosticket.domain.category.repository.CategoryRepository;
import kr.doridos.dosticket.domain.place.entity.Place;
import kr.doridos.dosticket.domain.place.repository.PlaceRepository;
import kr.doridos.dosticket.domain.ticket.dto.TicketCreateRequest;
import kr.doridos.dosticket.domain.ticket.entity.Ticket;
import kr.doridos.dosticket.domain.ticket.exception.OpenDateNotCorrectException;
import kr.doridos.dosticket.domain.ticket.exception.PlaceNotFoundException;
import kr.doridos.dosticket.domain.ticket.exception.UserNotTicketManagerException;
import kr.doridos.dosticket.domain.ticket.repository.TicketRepository;
import kr.doridos.dosticket.domain.user.User;
import kr.doridos.dosticket.domain.user.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class TicketManagerServiceTest {

    @InjectMocks
    private TicketManagerService ticketManagerService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    User user = getUser();
    User ticketManager = getTicketManager();
    Category category = getCategory();
    Place place = getPlace();
    TicketCreateRequest ticketCreateRequest = getTicketCreateRequest();

    @Test
    @DisplayName("티켓을 성공적으로 생성한다.")
    void createTicket_success() {
        given(categoryRepository.findById(ticketCreateRequest.getCategoryId())).willReturn(Optional.of(category));
        given(placeRepository.findById(ticketCreateRequest.getPlaceId())).willReturn(Optional.of(place));
        given(ticketRepository.save(any(Ticket.class))).willReturn(any());

        ticketManagerService.createTicket(ticketCreateRequest, ticketManager);

        then(ticketRepository).should().save(any(Ticket.class));
    }

    @Test
    @DisplayName("티켓생성시 티켓매니저가 아니라면 티켓생성에 실패한다.")
    void createTicket_userNotTicketManager_throwException() {
        assertThatThrownBy(() -> ticketManagerService.createTicket(ticketCreateRequest, user))
                .isInstanceOf(UserNotTicketManagerException.class)
                .hasMessage("티켓등록 권한이 없습니다.");
    }

    @Test
    @DisplayName("티켓 생성시 카테고리가 존재하지 않으면 예외가 발생한다.")
    void createTicket_notExistCategory_throwException() {
        given(categoryRepository.findById(ticketCreateRequest.getCategoryId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> ticketManagerService.createTicket(ticketCreateRequest, ticketManager))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("카테고리가 존재하지 않습니다.");

        then(categoryRepository).should().findById(ticketCreateRequest.getCategoryId());
    }

    @Test
    @DisplayName("티켓 생성시 장소가 존재하지 않으면 예외가 발생한다.")
    void createTicket_notExistPlace_throwException() {
        given(categoryRepository.findById(ticketCreateRequest.getCategoryId())).willReturn(Optional.of(category));
        given(placeRepository.findById(ticketCreateRequest.getPlaceId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> ticketManagerService.createTicket(ticketCreateRequest, ticketManager))
                .isInstanceOf(PlaceNotFoundException.class)
                .hasMessage("장소가 존재하지 않습니다.");

        then(placeRepository).should().findById(ticketCreateRequest.getPlaceId());
    }

    @Test
    @DisplayName("티켓 생성시 예약시작시간과 예약마감시간이 유효하지 않으면 예외가 발생한다.")
    void createTicket_notCorrectDate_throwException() {
            TicketCreateRequest ticketCreateRequest = TicketCreateRequest.builder()
                    .title("모차르트")
                    .content("모차르트 최고의 연주")
                    .runningTime("120분")
                    .openDate(LocalDateTime.of(2023, 7, 22, 12, 0))
                    .endDate(LocalDateTime.of(2023, 7, 21, 12, 0))
                    .startDate(LocalDateTime.of(2023, 7, 23, 13, 0))
                    .placeId(1L)
                    .categoryId(1L).build();

        assertThatThrownBy(() -> ticketManagerService.createTicket(ticketCreateRequest, ticketManager))
                .isInstanceOf(OpenDateNotCorrectException.class)
                .hasMessage("예매 시작일은 종료일 이후가 될 수 없습니다.");
    }

    private User getTicketManager() {
        return User.of("email@email", "123456a!", "호호", "12345", UserType.TICKET_MANAGER);
    }

    private User getUser() {
        return User.of("email@email", "123456a!", "호호", "12345", UserType.USER);
    }

    private Place getPlace() {
        return new Place(1L, "잠실경기장", "서울특별시", LocalDateTime.now(), LocalDateTime.now());
    }

    private Category getCategory() {
        return new Category(1L, "뮤지컬");
    }

    private TicketCreateRequest getTicketCreateRequest() {
        return TicketCreateRequest.builder()
                .title("모차르트")
                .content("모차르트 최고의 연주")
                .runningTime("120분")
                .openDate(LocalDateTime.of(2023, 7, 22, 12, 0))
                .endDate(LocalDateTime.of(2023, 7, 23, 12, 0))
                .startDate(LocalDateTime.of(2023, 7, 23, 13, 0))
                .placeId(1L)
                .categoryId(1L).build();
    }
}