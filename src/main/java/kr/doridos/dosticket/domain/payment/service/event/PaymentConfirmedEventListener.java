package kr.doridos.dosticket.domain.payment.service.event;

import kr.doridos.dosticket.domain.reservation.service.ReservationService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PaymentConfirmedEventListener {

    private final ReservationService reservationService;

    public PaymentConfirmedEventListener(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentConfirmedEvent(PaymentConfirmedEvent event) {
        reservationService.updateReservationStatusIsBooked(event.getReservationId());
    }
}
