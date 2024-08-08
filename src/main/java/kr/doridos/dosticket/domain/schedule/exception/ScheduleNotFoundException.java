package kr.doridos.dosticket.domain.schedule.exception;

import kr.doridos.dosticket.exception.BusinessException;
import kr.doridos.dosticket.exception.ErrorCode;

public class ScheduleNotFoundException extends BusinessException {
    public ScheduleNotFoundException(String message, ErrorCode errorCode) {
        super(message, ErrorCode.SCHEDULE_NOT_FOUND);
    }

    public ScheduleNotFoundException(ErrorCode errorCode) {
        super(ErrorCode.SCHEDULE_NOT_FOUND);
    }
}

