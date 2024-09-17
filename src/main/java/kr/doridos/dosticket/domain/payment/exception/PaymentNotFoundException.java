package kr.doridos.dosticket.domain.payment.exception;

import kr.doridos.dosticket.exception.BusinessException;
import kr.doridos.dosticket.exception.ErrorCode;

public class PaymentNotFoundException extends BusinessException {
    public PaymentNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public PaymentNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
