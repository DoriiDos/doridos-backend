package kr.doridos.dosticket.domain.schedule.fixture;

import kr.doridos.dosticket.domain.place.entity.Section;
import kr.doridos.dosticket.domain.schedule.entity.ScheduleSeat;

public class ScheduleSeatFixture {

    public static ScheduleSeat 좌석생성() {
        return ScheduleSeat.builder()
                .id(1L)
                .section(Section.A)
                .seatNum(1)
                .isReserved(false)
                .schedule(ScheduleFixture.스케줄_생성())
                .build();
    }

    public static ScheduleSeat 좌석생성2() {
        return ScheduleSeat.builder()
                .id(2L)
                .section(Section.B)
                .seatNum(1)
                .isReserved(false)
                .schedule(ScheduleFixture.스케줄_생성())
                .build();
    }

    public static ScheduleSeat 예약된_좌석생성() {
        return ScheduleSeat.builder()
                .id(3L)
                .isReserved(true)
                .schedule(ScheduleFixture.스케줄_생성())
                .build();
    }
}
