package kr.doridos.dosticket.domain.ticket.dto;

import kr.doridos.dosticket.domain.ticket.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TicketPageResponse {

    private Long id;
    private String title;
    private String content;
    private String runningTime;
    private LocalDateTime openDate;
    private LocalDateTime endDate;
    private LocalDateTime startDate;

    public static TicketPageResponse convertToDto(Ticket ticket) {
        return new TicketPageResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getRunningTime(),
                ticket.getRunningTime(),
                ticket.getOpenDate(),
                ticket.getEndDate(),
                ticket.getStartDate()
        );
    }
}




