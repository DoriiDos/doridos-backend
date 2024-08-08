package kr.doridos.dosticket.domain.schedule.fixture;

import kr.doridos.dosticket.domain.schedule.entity.Schedule;
import kr.doridos.dosticket.domain.ticket.fixture.TicketFixture;

import java.time.LocalDateTime;

public class ScheduleFixture {

    public static Schedule 스케줄_생성() {
        return Schedule.builder()
                .id(1L)
                .startTime(LocalDateTime.of(2024, 7, 22, 7, 0))
                .endTime(LocalDateTime.of(2024, 7, 23, 7, 0))
                .ticket(TicketFixture.티켓_생성())
                .build();
    }

    public static Schedule 스케줄_생성2() {
        return Schedule.builder()
                .id(2L)
                .startTime(LocalDateTime.of(2024, 7, 24, 7, 0))
                .endTime(LocalDateTime.of(2024, 7, 25, 7, 0))
                .ticket(TicketFixture.티켓_생성())
                .build();
    }

    public static Schedule 스케줄_생성3() {
        return Schedule.builder()
                .startTime(LocalDateTime.of(2024, 7, 24, 7, 0))
                .endTime(LocalDateTime.of(2024, 7, 25, 7, 0))
                .ticket(TicketFixture.티켓_생성3())
                .build();
    }
}
