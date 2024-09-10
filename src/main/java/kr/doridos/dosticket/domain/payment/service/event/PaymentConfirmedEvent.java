package kr.doridos.dosticket.domain.payment.service.event;

public class PaymentConfirmedEvent {

    private final Long reservationId;

    public PaymentConfirmedEvent(final Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getReservationId() {
        return reservationId;
    }
}
