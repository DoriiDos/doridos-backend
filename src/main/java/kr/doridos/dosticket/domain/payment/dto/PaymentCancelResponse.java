package kr.doridos.dosticket.domain.payment.dto;


import kr.doridos.dosticket.domain.payment.entity.PaymentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@NoArgsConstructor
public class PaymentCancelResponse {
    private String paymentKey;
    private String orderId;
    private String orderName;
    private PaymentStatus paymentStatus;
    private ZonedDateTime requestedAt;
    private ZonedDateTime approvedAt;

    public PaymentCancelResponse(String paymentKey, String orderId, String orderName, PaymentStatus paymentStatus, ZonedDateTime requestedAt, ZonedDateTime approvedAt) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.orderName = orderName;
        this.paymentStatus = paymentStatus;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
    }
}
