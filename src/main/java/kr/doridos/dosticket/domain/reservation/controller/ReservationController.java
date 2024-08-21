package kr.doridos.dosticket.domain.reservation.controller;

import kr.doridos.dosticket.domain.auth.support.jwt.UserDetailsImpl;
import kr.doridos.dosticket.domain.reservation.dto.RegisterReservationResponse;
import kr.doridos.dosticket.domain.reservation.dto.ReservationInfoResponse;
import kr.doridos.dosticket.domain.reservation.dto.ReservationRequest;
import kr.doridos.dosticket.domain.reservation.dto.ReservationResponse;
import kr.doridos.dosticket.domain.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReservationController {

    final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<RegisterReservationResponse> registerReservation(@AuthenticationPrincipal final UserDetailsImpl userDetails,
                                                                           @RequestBody final ReservationRequest request) {
        return ResponseEntity.ok(reservationService.registerReservation(userDetails.getUser().getId(), request));
    }

    @GetMapping("/reservations/me")
    public ResponseEntity<List<ReservationResponse>> findUserReservations(@AuthenticationPrincipal final UserDetailsImpl userDetails) {
        return ResponseEntity.ok(reservationService.findUserReservations(userDetails.getUser().getId()));
    }

    @GetMapping("/reservations/me/{reservationId}")
    public ResponseEntity<ReservationInfoResponse> getReservationInfo(@AuthenticationPrincipal final UserDetailsImpl userDetails,
                                                                      @PathVariable final Long reservationId) {
        return ResponseEntity.ok(reservationService.getReservationInfo(reservationId, userDetails.getUser().getId()));
    }
}
