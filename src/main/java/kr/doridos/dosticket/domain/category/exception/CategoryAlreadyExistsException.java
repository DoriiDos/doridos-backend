package kr.doridos.dosticket.domain.category.exception;

import kr.doridos.dosticket.exception.BusinessException;
import kr.doridos.dosticket.exception.ErrorCode;

public class CategoryAlreadyExistsException extends BusinessException {
    public CategoryAlreadyExistsException(String message, ErrorCode errorCode) {
        super(message, ErrorCode.CATEGORY_EXIST);
    }

    public CategoryAlreadyExistsException(ErrorCode errorCode) {
        super(ErrorCode.CATEGORY_EXIST);
    }
}
