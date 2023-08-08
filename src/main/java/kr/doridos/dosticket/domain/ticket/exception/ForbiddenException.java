package kr.doridos.dosticket.domain.ticket.exception;

import kr.doridos.dosticket.exception.BusinessException;
import kr.doridos.dosticket.exception.ErrorCode;

public class ForbiddenException extends BusinessException {

    public ForbiddenException(String message, ErrorCode errorCode) {
        super(message, ErrorCode.FORBIDDEN_USER);
    }

    public ForbiddenException(ErrorCode errorCode) {
        super(ErrorCode.FORBIDDEN_USER);
    }
}
