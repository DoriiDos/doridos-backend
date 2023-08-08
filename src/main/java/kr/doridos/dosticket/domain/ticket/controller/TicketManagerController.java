package kr.doridos.dosticket.domain.ticket.controller;

import kr.doridos.dosticket.domain.auth.support.jwt.UserDetailsImpl;
import kr.doridos.dosticket.domain.ticket.dto.TicketCreateRequest;
import kr.doridos.dosticket.domain.ticket.dto.TicketUpdateRequest;
import kr.doridos.dosticket.domain.ticket.service.TicketManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/manager")
public class TicketManagerController {

    private final TicketManagerService ticketManagerService;

    public TicketManagerController(final TicketManagerService ticketManagerService) {
        this.ticketManagerService = ticketManagerService;
    }

    @PostMapping("/tickets")
    public ResponseEntity<Void> createTicket(@RequestBody final TicketCreateRequest request, @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        final Long ticketId = ticketManagerService.createTicket(request, userDetails.getUser());
        return ResponseEntity.created(URI.create("/tickets" + ticketId)).build();
    }

    @PatchMapping("/tickets/{ticketId}")
    public ResponseEntity<Void> updateTicket(@PathVariable("ticketId") final Long ticketId,
                                             @RequestBody final TicketUpdateRequest request,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ticketManagerService.updateTicket(ticketId, request, userDetails.getUser());
        return ResponseEntity.noContent().build();
    }
}
