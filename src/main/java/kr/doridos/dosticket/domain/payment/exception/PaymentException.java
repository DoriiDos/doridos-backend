package kr.doridos.dosticket.domain.payment.exception;

import org.springframework.http.HttpStatus;

public class PaymentException extends RuntimeException {
    private final String code;
    private final HttpStatus status;

    public PaymentException(String message, String code, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
