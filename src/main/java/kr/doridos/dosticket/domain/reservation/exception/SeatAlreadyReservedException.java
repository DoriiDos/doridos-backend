package kr.doridos.dosticket.domain.reservation.exception;

import kr.doridos.dosticket.exception.BusinessException;
import kr.doridos.dosticket.exception.ErrorCode;

public class SeatAlreadyReservedException extends BusinessException {
    public SeatAlreadyReservedException(ErrorCode errorCode) {
        super(ErrorCode.SEAT_ALREADY_RESERVED);
    }
}
