package kr.doridos.dosticket.domain.reservation.dto;

import com.querydsl.core.annotations.QueryProjection;
import kr.doridos.dosticket.domain.schedule.entity.ScheduleSeat;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ReservationInfoResponse {

    private final Long reservationId;
    private final String title;
    private final String content;
    private final String runningTime;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final List<Long> seatsId;

    @QueryProjection
    public ReservationInfoResponse(Long reservationId, String title, String content, String runningTime, LocalDateTime startDate, LocalDateTime endDate, List<Long> seatsId) {
        this.reservationId = reservationId;
        this.title = title;
        this.content = content;
        this.runningTime = runningTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.seatsId = seatsId;
    }
}
