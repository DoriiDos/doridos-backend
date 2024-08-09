package kr.doridos.dosticket.domain.ticket.repository;

import kr.doridos.dosticket.domain.ticket.dto.TicketPageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface TicketCustomRepository {

    Page<TicketPageResponse> findTicketsByCategoryId(Long categoryId, Pageable pageable);

    Page<TicketPageResponse> findTicketsByStartDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

}
