package kr.doridos.dosticket.domain.reservation.dto;

import kr.doridos.dosticket.domain.reservation.entity.Reservation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterReservationResponse {

    private Long reservationId;

    public RegisterReservationResponse(final Long reservationId) {
        this.reservationId = reservationId;
    }

    public static RegisterReservationResponse of(final Reservation reservation) {
        return new RegisterReservationResponse(reservation.getId());
    }
}
