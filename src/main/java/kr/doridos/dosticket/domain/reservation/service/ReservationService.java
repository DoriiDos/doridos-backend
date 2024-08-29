package kr.doridos.dosticket.domain.reservation.service;

import kr.doridos.dosticket.domain.reservation.dto.RegisterReservationResponse;
import kr.doridos.dosticket.domain.reservation.dto.ReservationInfoResponse;
import kr.doridos.dosticket.domain.reservation.dto.ReservationRequest;
import kr.doridos.dosticket.domain.reservation.dto.ReservationResponse;
import kr.doridos.dosticket.domain.reservation.entity.ReservationStatus;
import kr.doridos.dosticket.domain.reservation.exception.ReservationNotCollectUserException;
import kr.doridos.dosticket.domain.reservation.exception.ReservationNotFoundException;
import kr.doridos.dosticket.domain.reservation.exception.SeatNotFoundException;
import kr.doridos.dosticket.domain.reservation.entity.Reservation;
import kr.doridos.dosticket.domain.reservation.exception.SeatAlreadyReservedException;
import kr.doridos.dosticket.domain.reservation.repository.ReservationRepository;
import kr.doridos.dosticket.domain.schedule.entity.ScheduleSeat;
import kr.doridos.dosticket.domain.schedule.repository.ScheduleSeatRepository;
import kr.doridos.dosticket.exception.ErrorCode;
import kr.doridos.dosticket.global.redis.DistributedLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class ReservationService {

    private final ScheduleSeatRepository scheduleSeatRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(final ScheduleSeatRepository scheduleSeatRepository, final ReservationRepository reservationRepository) {
        this.scheduleSeatRepository = scheduleSeatRepository;
        this.reservationRepository = reservationRepository;
    }

    @DistributedLock(key = "#request.seatIds")
    public RegisterReservationResponse registerReservation(final Long userId, final ReservationRequest request) {
        final List<ScheduleSeat> seats = scheduleSeatRepository.findAllById(request.getSeatIds());
        validateSeatsSize(request.getSeatIds(), seats);
        validateSeatsIsReserve(seats);

        seats.forEach(ScheduleSeat::reserveSeatStatus);

        final Reservation reservation = Reservation.builder()
                .scheduleId(request.getScheduleId())
                .ticketId(request.getTicketId())
                .seats(seats)
                .reservationStatus(ReservationStatus.PAYMENT_WAITING)
                .userId(userId)
                .build();
        reservationRepository.save(reservation);
        return RegisterReservationResponse.of(reservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findUserReservations(final Long userId) {
        return reservationRepository.findReservationsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public ReservationInfoResponse getReservationInfo(final Long reservationId, final Long userId) {
        final Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> { throw new ReservationNotFoundException(ErrorCode.RESERVATION_NOT_FOUND); });

        validateReservationOwnerShip(reservation.getUserId(), userId);

        return reservationRepository.findByReservationInfo(reservation);
    }

    private void validateReservationOwnerShip(Long reservationUserId, Long userId) {
        if (!reservationUserId.equals(userId)) {
            throw new ReservationNotCollectUserException(ErrorCode.RESERVATION_NOT_OWNER);
        }
    }

    private void validateSeatsSize(List<Long> seatsId, List<ScheduleSeat> seats) {
        if (seats.size() == 0 || seats.size() != seatsId.size()) {
            throw new SeatNotFoundException(ErrorCode.SEAT_NOT_FOUND);
        }
    }

    private void validateSeatsIsReserve(List<ScheduleSeat> seats) {
        if (seats.stream().anyMatch(ScheduleSeat::isReserved)) {
            throw new SeatAlreadyReservedException(ErrorCode.SEAT_ALREADY_RESERVED);
        }
    }
}
