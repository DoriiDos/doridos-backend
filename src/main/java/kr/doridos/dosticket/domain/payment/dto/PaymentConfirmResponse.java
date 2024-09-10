package kr.doridos.dosticket.domain.payment.dto;

import kr.doridos.dosticket.domain.payment.entity.Payment;
import kr.doridos.dosticket.domain.payment.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentConfirmResponse {

    private String paymentKey;
    private String orderId;
    private String orderName;
    private int totalAmount;
    private ZonedDateTime requestedAt;
    private ZonedDateTime approvedAt;
    private PaymentStatus status;

    public Payment toPayment(Long reservationId, Long userId) {
        return new Payment(paymentKey, orderId, orderName, totalAmount, requestedAt, approvedAt, reservationId, status, userId);
    }
}
