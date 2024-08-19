package kr.doridos.dosticket.global.redis.exception;

import kr.doridos.dosticket.exception.BusinessException;
import kr.doridos.dosticket.exception.ErrorCode;

public class LockFailException extends BusinessException {

    public LockFailException(ErrorCode errorCode) {
        super(ErrorCode.LOCK_ACQUISITION_FAILED);
    }
}
