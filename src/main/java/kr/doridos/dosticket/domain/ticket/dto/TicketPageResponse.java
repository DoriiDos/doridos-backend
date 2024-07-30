package kr.doridos.dosticket.domain.ticket.dto;

import com.querydsl.core.annotations.QueryProjection;
import kr.doridos.dosticket.domain.ticket.entity.Ticket;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TicketPageResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String runningTime;
    private final LocalDateTime openDate;
    private final LocalDateTime endDate;
    private final LocalDateTime startDate;

    @QueryProjection
    public TicketPageResponse(Long id,
                              String title,
                              String content,
                              String runningTime,
                              LocalDateTime openDate,
                              LocalDateTime endDate,
                              LocalDateTime startDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.runningTime = runningTime;
        this.openDate = openDate;
        this.endDate = endDate;
        this.startDate = startDate;
    }


    public static TicketPageResponse convertToDto(Ticket ticket) {
        return new TicketPageResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getContent(),
                ticket.getRunningTime(),
                ticket.getOpenDate(),
                ticket.getEndDate(),
                ticket.getStartDate()
        );
    }
}




