package kr.doridos.dosticket.domain.auth.exception;

import kr.doridos.dosticket.exception.BusinessException;
import kr.doridos.dosticket.exception.ErrorCode;

public class OAuthNotSupportException extends BusinessException {

    public OAuthNotSupportException() {
        super(ErrorCode.NOT_SUPPORT_OAUTH_CLIENT);
    }

    public OAuthNotSupportException(ErrorCode errorCode) {
        super(ErrorCode.NOT_SUPPORT_OAUTH_CLIENT);
    }
}
