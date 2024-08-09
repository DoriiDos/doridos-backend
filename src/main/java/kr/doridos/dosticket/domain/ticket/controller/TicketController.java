package kr.doridos.dosticket.domain.ticket.controller;

import kr.doridos.dosticket.domain.ticket.dto.TicketInfoResponse;
import kr.doridos.dosticket.domain.ticket.dto.TicketPageResponse;
import kr.doridos.dosticket.domain.ticket.service.TicketService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(final TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketInfoResponse> ticketInfo(@PathVariable final Long ticketId) {
        final TicketInfoResponse response = ticketService.ticketInfo(ticketId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/date")
    public ResponseEntity<Page<TicketPageResponse>> findTicketsByStartDate(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate startDate,
                                                                           @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate endDate,
                                                                           final Pageable pageable) {
        return ResponseEntity.ok(ticketService.findTicketsByDate(startDate, endDate, pageable));
    }

    @GetMapping
    public ResponseEntity<Page<TicketPageResponse>> findAllTicketsWithPaging(
            @PageableDefault(size = 10, page = 0) final Pageable pageable) {
        return ResponseEntity.ok(ticketService.findAllTickets(pageable));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<TicketPageResponse>> findTicketsByCategoryId (
            @PathVariable("categoryId") final Long categoryId,
            @PageableDefault(size = 10, page = 0) final Pageable pageable) {
        return ResponseEntity.ok(ticketService.findTicketsByCategoryId(categoryId, pageable));
    }
}
