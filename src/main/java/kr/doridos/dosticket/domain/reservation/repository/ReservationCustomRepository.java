package kr.doridos.dosticket.domain.reservation.repository;

import kr.doridos.dosticket.domain.reservation.dto.ReservationResponse;

import java.util.List;

public interface ReservationCustomRepository {

    List<ReservationResponse> findReservationsByUserId(final Long userId);

}
