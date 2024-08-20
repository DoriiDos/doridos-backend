package kr.doridos.dosticket.domain.reservation.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReservationResponse {

    private final Long reservationId;
    private final String title;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    @QueryProjection
    public ReservationResponse(Long reservationId, String title, LocalDateTime startDate, LocalDateTime endDate) {
        this.reservationId = reservationId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
