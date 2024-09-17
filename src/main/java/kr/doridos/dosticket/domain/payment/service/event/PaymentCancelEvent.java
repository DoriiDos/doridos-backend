package kr.doridos.dosticket.domain.payment.service.event;

public class PaymentCancelEvent {

    private final Long reservationId;

    public PaymentCancelEvent(final Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getReservationId() {
        return reservationId;
    }
}
