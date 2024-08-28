package kr.doridos.dosticket.domain.ticket.repository;

import kr.doridos.dosticket.domain.ticket.dto.TicketPageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface TicketCustomRepository {

    Page<TicketPageResponse> findFilteredTickets(LocalDate startDate, LocalDate endDate, Long categoryId, Pageable pageable);

}
