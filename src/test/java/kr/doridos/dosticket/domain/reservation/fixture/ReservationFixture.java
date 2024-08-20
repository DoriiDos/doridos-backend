package kr.doridos.dosticket.domain.reservation.fixture;

import kr.doridos.dosticket.domain.reservation.entity.Reservation;

public class ReservationFixture {
    public static Reservation 예매생성() {
        return Reservation.builder()
                .id(1L)
                .scheduleId(1L)
                .ticketId(1L)
                .userId(1L)
                .build();
    }
}
