package kr.doridos.dosticket.domain.ticket.dto;

import kr.doridos.dosticket.domain.ticket.entity.Ticket;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketInfoResponse {

    private String title;
    private String content;
    private String runningTime;
    private LocalDateTime openDate;
    private LocalDateTime endDate;
    private LocalDateTime startDate;
    private String place;
    private String categoryName;

    public TicketInfoResponse(final String title,
                              final String content,
                              final String runningTime,
                              final LocalDateTime openDate,
                              final LocalDateTime endDate,
                              final LocalDateTime startDate,
                              final String place,
                              final String categoryName) {
        this.title = title;
        this.content = content;
        this.runningTime = runningTime;
        this.openDate = openDate;
        this.endDate = endDate;
        this.startDate = startDate;
        this.place = place;
        this.categoryName = categoryName;
    }

    public static TicketInfoResponse of(final Ticket ticket) {
        return new TicketInfoResponse(
                ticket.getTitle(),
                ticket.getContent(),
                ticket.getRunningTime(),
                ticket.getOpenDate(),
                ticket.getEndDate(),
                ticket.getStartDate(),
                ticket.getPlace().getName(),
                ticket.getCategory().getName()
        );
    }
}
