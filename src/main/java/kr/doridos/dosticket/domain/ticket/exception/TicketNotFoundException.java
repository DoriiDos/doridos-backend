package kr.doridos.dosticket.domain.ticket.exception;

import kr.doridos.dosticket.exception.BusinessException;
import kr.doridos.dosticket.exception.ErrorCode;

public class TicketNotFoundException extends BusinessException {

    public TicketNotFoundException(String message, ErrorCode errorCode) {
        super(message, ErrorCode.TICKET_NOT_FOUND);
    }

    public TicketNotFoundException(ErrorCode errorCode) {
        super(ErrorCode.TICKET_NOT_FOUND);
    }
}
