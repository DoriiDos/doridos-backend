package kr.doridos.dosticket.domain.schedule.dto;

import kr.doridos.dosticket.domain.schedule.entity.ScheduleSeat;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ScheduleSeatResponse {

    private final Long id;
    private final boolean isReserved;

    public ScheduleSeatResponse(final Long id,final boolean isReserved) {
        this.id = id;
        this.isReserved = isReserved;
    }

    public static ScheduleSeatResponse fromEntity(final ScheduleSeat scheduleSeat) {
        return new ScheduleSeatResponse(
                scheduleSeat.getId(),
                scheduleSeat.isReserved()
        );
    }

    public static List<ScheduleSeatResponse> from(final List<ScheduleSeat> schedules) {
        return schedules.stream()
                .map(ScheduleSeatResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
