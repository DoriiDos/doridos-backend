package kr.doridos.dosticket.global.redis.exception;

import kr.doridos.dosticket.exception.BusinessException;
import kr.doridos.dosticket.exception.ErrorCode;

public class LockInterruptedException extends BusinessException {

    public LockInterruptedException(ErrorCode errorCode) {
        super(ErrorCode.LOCK_INTERRUPTED);
    }
}
