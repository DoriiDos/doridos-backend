package kr.doridos.dosticket.domain.reservation.repository;

import kr.doridos.dosticket.domain.reservation.dto.ReservationInfoResponse;
import kr.doridos.dosticket.domain.reservation.dto.ReservationResponse;
import kr.doridos.dosticket.domain.reservation.entity.Reservation;

import java.util.List;

public interface ReservationCustomRepository {

    List<ReservationResponse> findReservationsByUserId(final Long userId);
    ReservationInfoResponse findByReservationInfo(final Reservation reservation);

}
