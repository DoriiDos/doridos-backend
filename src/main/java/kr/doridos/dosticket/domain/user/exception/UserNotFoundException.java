package kr.doridos.dosticket.domain.user.exception;

import kr.doridos.dosticket.exception.BusinessException;
import kr.doridos.dosticket.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(String message, ErrorCode errorCode) {
        super(message, ErrorCode.USER_NOT_FOUND);
    }

    public UserNotFoundException(ErrorCode errorCode) {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
