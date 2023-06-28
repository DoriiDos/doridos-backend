package kr.doridos.dosticket.domain.auth.exception;

import kr.doridos.dosticket.exception.BusinessException;
import kr.doridos.dosticket.exception.ErrorCode;

public class InvalidTokenException extends BusinessException {

    public InvalidTokenException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public InvalidTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
