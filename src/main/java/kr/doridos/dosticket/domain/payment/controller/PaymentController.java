package kr.doridos.dosticket.domain.payment.controller;

import kr.doridos.dosticket.domain.auth.support.jwt.UserDetailsImpl;
import kr.doridos.dosticket.domain.payment.dto.PaymentConfirmRequest;
import kr.doridos.dosticket.domain.payment.dto.PaymentConfirmResponse;
import kr.doridos.dosticket.domain.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/api/{reservationId}/payment")
    public ResponseEntity<PaymentConfirmResponse> confirm(@RequestBody final PaymentConfirmRequest paymentConfirmRequest,
                                                          @PathVariable("reservationId") final Long reservationId,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        final PaymentConfirmResponse paymentConfirmResponse = paymentService.confirmPayment(paymentConfirmRequest, reservationId, userDetails.getUser().getId());
        return ResponseEntity.ok(paymentConfirmResponse);
    }
}
