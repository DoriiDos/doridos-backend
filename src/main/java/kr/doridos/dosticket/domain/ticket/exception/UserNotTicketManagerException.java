package kr.doridos.dosticket.domain.ticket.exception;

import kr.doridos.dosticket.exception.BusinessException;
import kr.doridos.dosticket.exception.ErrorCode;

public class UserNotTicketManagerException extends BusinessException {

    public UserNotTicketManagerException(String message, ErrorCode errorCode) {
        super(message, ErrorCode.NOT_TICKET_MANAGER);
    }

    public UserNotTicketManagerException(ErrorCode errorCode) {
        super(ErrorCode.NOT_TICKET_MANAGER);
    }
}
