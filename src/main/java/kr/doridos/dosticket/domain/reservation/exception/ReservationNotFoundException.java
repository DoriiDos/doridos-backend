package kr.doridos.dosticket.domain.reservation.exception;

import kr.doridos.dosticket.exception.BusinessException;
import kr.doridos.dosticket.exception.ErrorCode;

public class ReservationNotFoundException extends BusinessException {
    public ReservationNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
