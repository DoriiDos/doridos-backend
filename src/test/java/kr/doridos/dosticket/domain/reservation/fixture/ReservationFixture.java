package kr.doridos.dosticket.domain.reservation.fixture;

import kr.doridos.dosticket.domain.reservation.entity.Reservation;
import kr.doridos.dosticket.domain.schedule.fixture.ScheduleFixture;
import kr.doridos.dosticket.domain.schedule.fixture.ScheduleSeatFixture;

import java.util.List;

public class ReservationFixture {
    public static Reservation 예매생성() {
        return Reservation.builder()
                .id(1L)
                .scheduleId(1L)
                .seats(List.of(ScheduleSeatFixture.좌석생성(), ScheduleSeatFixture.좌석생성2()))
                .ticketId(1L)
                .userId(1L)
                .build();
    }
}
