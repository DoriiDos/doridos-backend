package kr.doridos.dosticket.domain.schedule.dto;

import kr.doridos.dosticket.domain.schedule.entity.Schedule;
import lombok.Getter;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ScheduleResponse {

    private final Long id;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public ScheduleResponse(final Long id, final LocalDateTime startTime, final LocalDateTime endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static ScheduleResponse fromEntity(final Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getStartDate(),
                schedule.getEndDate()
        );
    }

    public static List<ScheduleResponse> from(final List<Schedule> schedules) {
        return schedules.stream()
                .map(ScheduleResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
