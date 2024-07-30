package kr.doridos.dosticket.domain.ticket.repository;

import kr.doridos.dosticket.domain.ticket.dto.TicketPageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TicketCustomRepository {

    Page<TicketPageResponse> findTicketsByCategoryId(Long categoryId, Pageable pageable);

}
