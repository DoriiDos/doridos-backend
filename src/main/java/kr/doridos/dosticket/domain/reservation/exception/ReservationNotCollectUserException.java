package kr.doridos.dosticket.domain.reservation.exception;

import kr.doridos.dosticket.exception.BusinessException;
import kr.doridos.dosticket.exception.ErrorCode;

public class ReservationNotCollectUserException extends BusinessException {
    public ReservationNotCollectUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
