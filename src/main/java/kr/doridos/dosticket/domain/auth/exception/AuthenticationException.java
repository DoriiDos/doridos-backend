package kr.doridos.dosticket.domain.auth.exception;

import kr.doridos.dosticket.exception.BusinessException;
import kr.doridos.dosticket.exception.ErrorCode;

public class AuthenticationException extends BusinessException {

    public AuthenticationException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
