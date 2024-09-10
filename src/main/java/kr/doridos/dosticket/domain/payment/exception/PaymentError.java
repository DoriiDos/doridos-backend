package kr.doridos.dosticket.domain.payment.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentError {
    private String code;
    private String message;
}
