package kr.doridos.dosticket.domain.ticket.exception;

import kr.doridos.dosticket.exception.BusinessException;
import kr.doridos.dosticket.exception.ErrorCode;

public class OpenDateNotCorrectException extends BusinessException {

    public OpenDateNotCorrectException(String message, ErrorCode errorCode) {
        super(message, ErrorCode.DATE_NOT_CORRECT);
    }

    public OpenDateNotCorrectException(ErrorCode errorCode) {
        super(ErrorCode.DATE_NOT_CORRECT);
    }
}
