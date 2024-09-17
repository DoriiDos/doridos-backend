package kr.doridos.dosticket.domain.payment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String paymentKey;
    private String orderId;
    private String orderName;
    private int amount;
    private ZonedDateTime requestedAt;
    private ZonedDateTime approvedAt;
    private Long reservationId;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private Long userId;

    public Payment(String paymentKey, String orderId, String orderName, int amount, ZonedDateTime requestedAt, ZonedDateTime approvedAt,
                   Long reservationId, PaymentStatus status, Long userId) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
        this.orderName = orderName;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.reservationId = reservationId;
        this.status = status;
        this.userId = userId;
    }

    public void changeStatus(PaymentStatus status, ZonedDateTime requestedAt, ZonedDateTime approvedAt) {
        this.status = status;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
    }
}
