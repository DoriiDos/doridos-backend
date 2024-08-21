package kr.doridos.dosticket.domain.reservation.service;

import kr.doridos.dosticket.domain.reservation.dto.RegisterReservationResponse;
import kr.doridos.dosticket.domain.reservation.dto.ReservationInfoResponse;
import kr.doridos.dosticket.domain.reservation.dto.ReservationRequest;
import kr.doridos.dosticket.domain.reservation.dto.ReservationResponse;
import kr.doridos.dosticket.domain.reservation.entity.Reservation;
import kr.doridos.dosticket.domain.reservation.exception.ReservationNotCollectUserException;
import kr.doridos.dosticket.domain.reservation.exception.ReservationNotFoundException;
import kr.doridos.dosticket.domain.reservation.exception.SeatAlreadyReservedException;
import kr.doridos.dosticket.domain.reservation.exception.SeatNotFoundException;
import kr.doridos.dosticket.domain.reservation.repository.ReservationRepository;
import kr.doridos.dosticket.domain.schedule.entity.ScheduleSeat;
import kr.doridos.dosticket.domain.schedule.fixture.ScheduleSeatFixture;
import kr.doridos.dosticket.domain.schedule.repository.ScheduleSeatRepository;
import kr.doridos.dosticket.exception.ErrorCode;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReservationServiceTest {

    @Mock
    private ScheduleSeatRepository scheduleSeatRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void 티켓_좌석_예매에_성공한다() {
        List<ScheduleSeat> seats = List.of(ScheduleSeatFixture.좌석생성());
        ReservationRequest request = new ReservationRequest(1L, 1L, List.of(1L));

        given(scheduleSeatRepository.findAllById(request.getSeatIds())).willReturn(seats);

        RegisterReservationResponse response = reservationService.registerReservation(1L, request);

        assertThat(response).isNotNull();
    }

    @Test
    void 이미_예약된_좌석이면_예외가_발생한다() {
        ScheduleSeat scheduleSeat = ScheduleSeat.builder()
                .id(1L)
                .isReserved(true)
                .build();
        List<ScheduleSeat> seats = List.of(scheduleSeat);
        ReservationRequest request = new ReservationRequest(1L, 1L, List.of(1L));

        given(scheduleSeatRepository.findAllById(request.getSeatIds())).willReturn(seats);

        assertThatThrownBy(() -> reservationService.registerReservation(1L, request))
                .isInstanceOf(SeatAlreadyReservedException.class)
                .hasMessage("이미 예약된 좌석입니다.");
    }

    @Test
    void 좌석을_선택하지_않은_경우_예외가_발생한다() {
        List<ScheduleSeat> seats = List.of();
        ReservationRequest request = new ReservationRequest(1L, 1L, List.of());

        given(scheduleSeatRepository.findAllById(request.getSeatIds())).willReturn(seats);

        assertThatThrownBy(() -> reservationService.registerReservation(1L, request))
                .isInstanceOf(SeatNotFoundException.class)
                .hasMessage("좌석이 존재하지 않습니다.");

    }

    @Test
    void 유저의_예매내역을_조회한다() {
        Long userId = 1L;
        List<ReservationResponse> reservationResponses = List.of(
                new ReservationResponse(1L, "Ticket 1", LocalDateTime.now(), LocalDateTime.now().plusHours(2)));

        given(reservationRepository.findReservationsByUserId(userId)).willReturn(reservationResponses);

        List<ReservationResponse> responses = reservationService.findUserReservations(userId);

        assertThat(responses).isEqualTo(reservationResponses);
    }

    @Test
    void 예매_상새내역을_조회한다() {
        Long reservationId = 1L;
        Long userId = 1L;
        Reservation reservation = Reservation.builder()
                .ticketId(1L)
                .userId(userId)
                .scheduleId(1L)
                .build();
        ReservationInfoResponse expectedResponse = new ReservationInfoResponse(1L, "모차르트", "모차르트입니다.", "120분", LocalDateTime.now(), LocalDateTime.now().plusHours(2), List.of(1L));

        given(reservationRepository.findById(reservationId)).willReturn(Optional.of(reservation));
        given(reservationRepository.findByReservationInfo(reservation)).willReturn(expectedResponse);

        ReservationInfoResponse response = reservationService.getReservationInfo(reservationId, userId);

        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    void 예매가_존재하지_않으면_예외를_던진다() {
        Long reservationId = 1L;
        Long userId = 1L;

        given(reservationRepository.findById(reservationId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.getReservationInfo(reservationId, userId))
                .isInstanceOf(ReservationNotFoundException.class)
                .hasMessage(ErrorCode.RESERVATION_NOT_FOUND.getMessage());
    }

    @Test
    void 예매의_소유자가_아니면_예외를_던진다() {
        Long reservationId = 1L;
        Long userId = 2L;

        Reservation reservation = Reservation.builder()
                .ticketId(1L)
                .userId(1L)
                .scheduleId(1L)
                .build();

        given(reservationRepository.findById(reservationId)).willReturn(Optional.of(reservation));

        assertThatThrownBy(() -> reservationService.getReservationInfo(reservationId, userId))
                .isInstanceOf(ReservationNotCollectUserException.class)
                .hasMessage(ErrorCode.RESERVATION_NOT_OWNER.getMessage());
    }
}