package kr.doridos.dosticket.domain.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentCancelRequest {

    private String cancelReason;

    public PaymentCancelRequest(String cancelReason) {
        this.cancelReason = cancelReason;
    }
}
