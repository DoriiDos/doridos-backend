package kr.doridos.dosticket.domain.ticket.repository;

import kr.doridos.dosticket.domain.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
