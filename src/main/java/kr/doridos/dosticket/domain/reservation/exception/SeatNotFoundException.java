package kr.doridos.dosticket.domain.reservation.exception;

import kr.doridos.dosticket.exception.BusinessException;
import kr.doridos.dosticket.exception.ErrorCode;

public class SeatNotFoundException extends BusinessException {

    public SeatNotFoundException(ErrorCode errorCode) {
        super(ErrorCode.SEAT_NOT_FOUND);
    }
}
