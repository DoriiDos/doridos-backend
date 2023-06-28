package kr.doridos.dosticket.domain.auth.exception;

import kr.doridos.dosticket.exception.BusinessException;
import kr.doridos.dosticket.exception.ErrorCode;

public class SignInFailureException extends BusinessException {

    public SignInFailureException(String message, ErrorCode errorCode) {
        super(message, ErrorCode.SIGN_IN_FAIL);
    }

    public SignInFailureException(ErrorCode errorCode) {
        super(ErrorCode.SIGN_IN_FAIL);
    }
}
