package kr.doridos.dosticket.domain.reservation.service;

import kr.doridos.dosticket.domain.reservation.dto.RegisterReservationResponse;
import kr.doridos.dosticket.domain.reservation.dto.ReservationRequest;
import kr.doridos.dosticket.domain.reservation.exception.SeatNotFoundException;
import kr.doridos.dosticket.domain.reservation.entity.Reservation;
import kr.doridos.dosticket.domain.reservation.exception.SeatAlreadyReservedException;
import kr.doridos.dosticket.domain.reservation.repository.ReservationRepository;
import kr.doridos.dosticket.domain.schedule.entity.ScheduleSeat;
import kr.doridos.dosticket.domain.schedule.repository.ScheduleSeatRepository;
import kr.doridos.dosticket.exception.ErrorCode;
import kr.doridos.dosticket.global.redis.DistributedLock;
import org.springframework.stereotype.Service;


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
                .userId(userId)
                .build();
        reservationRepository.save(reservation);
        return RegisterReservationResponse.of(reservation);
    }

    private void validateSeatsSize(List<Long> seatsId, List<ScheduleSeat> seats) {
        if (seats.size() == 0 || seats.size() != seatsId.size()) {
            throw new SeatNotFoundException(ErrorCode.SEAT_NOT_FOUND);
        }
    }

    private void validateSeatsIsReserve(List<ScheduleSeat> seats) {
        seats.forEach(seat -> {
            if (seat.isReserved()) {
                throw new SeatAlreadyReservedException(ErrorCode.SEAT_ALREADY_RESERVED);
            }
        });
    }
}
