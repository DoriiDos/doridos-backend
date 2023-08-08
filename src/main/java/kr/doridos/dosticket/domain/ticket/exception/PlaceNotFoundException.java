package kr.doridos.dosticket.domain.ticket.exception;

import kr.doridos.dosticket.exception.BusinessException;
import kr.doridos.dosticket.exception.ErrorCode;

public class PlaceNotFoundException extends BusinessException {
    public PlaceNotFoundException(String message, ErrorCode errorCode) {
        super(message, ErrorCode.PLACE_NOT_FOUND);
    }

    public PlaceNotFoundException(ErrorCode errorCode) {
        super(ErrorCode.PLACE_NOT_FOUND);
    }
}
