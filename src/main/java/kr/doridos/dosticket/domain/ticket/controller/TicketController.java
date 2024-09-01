package kr.doridos.dosticket.domain.ticket.controller;

import kr.doridos.dosticket.domain.ticket.dto.TicketInfoResponse;
import kr.doridos.dosticket.domain.ticket.dto.TicketPageResponse;
import kr.doridos.dosticket.domain.ticket.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(final TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketInfoResponse> ticketInfo(@PathVariable("ticketId") final Long ticketId) {
        final TicketInfoResponse response = ticketService.ticketInfo(ticketId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public Page<TicketPageResponse> getFilteredTickets(
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate endDate,
            @RequestParam(name = "categoryId", required = false) final Long categoryId,
            final Pageable pageable) {

        return ticketService.getFilteredTickets(startDate, endDate, categoryId, pageable);
    }
}
