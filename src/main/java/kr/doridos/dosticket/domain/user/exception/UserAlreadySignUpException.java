package kr.doridos.dosticket.domain.user.exception;

import kr.doridos.dosticket.exception.BusinessException;
import kr.doridos.dosticket.exception.ErrorCode;

public class UserAlreadySignUpException extends BusinessException {
    public UserAlreadySignUpException(String message, ErrorCode errorCode) {
        super(message, ErrorCode.USER_ALREADY_SIGNUP);
    }

    public UserAlreadySignUpException(ErrorCode errorCode) {
        super(ErrorCode.USER_ALREADY_SIGNUP);
    }
}
