package kr.doridos.dosticket.domain.reservation.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationRequest {

    private Long scheduleId;
    private Long ticketId;
    private List<Long> seatIds;

}
