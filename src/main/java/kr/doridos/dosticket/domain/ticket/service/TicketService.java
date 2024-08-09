package kr.doridos.dosticket.domain.ticket.service;

import kr.doridos.dosticket.domain.ticket.dto.TicketInfoResponse;
import kr.doridos.dosticket.domain.ticket.dto.TicketPageResponse;
import kr.doridos.dosticket.domain.ticket.entity.Ticket;
import kr.doridos.dosticket.domain.ticket.exception.TicketNotFoundException;
import kr.doridos.dosticket.domain.ticket.repository.TicketRepository;
import kr.doridos.dosticket.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(final TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Transactional(readOnly = true)
    public TicketInfoResponse ticketInfo(final Long ticketId) {
        final Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> {
                    throw new TicketNotFoundException(ErrorCode.TICKET_NOT_FOUND);});

        return TicketInfoResponse.of(ticket);
    }

    @Transactional(readOnly = true)
    public Page<TicketPageResponse> findTicketsByDate(final LocalDate startDate, final LocalDate endDate, final Pageable pageable) {
        return ticketRepository.findTicketsByStartDateBetween(startDate, endDate, pageable);
    }

    @Transactional(readOnly = true)
    public Page<TicketPageResponse> findAllTickets(final Pageable pageable) {
        return ticketRepository.findAll(pageable)
                .map(TicketPageResponse::convertToDto);
    }

    @Transactional(readOnly = true)
    public Page<TicketPageResponse> findTicketsByCategoryId (final Long categoryId, final Pageable pageable) {
        return ticketRepository.findTicketsByCategoryId(categoryId, pageable);
    }
}
