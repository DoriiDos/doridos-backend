package kr.doridos.dosticket.domain.payment.exception;

import kr.doridos.dosticket.exception.BusinessException;
import kr.doridos.dosticket.exception.ErrorCode;

public class PaymentAlreadyProcessedException extends BusinessException {

    public PaymentAlreadyProcessedException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public PaymentAlreadyProcessedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
