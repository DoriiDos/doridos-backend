package kr.doridos.dosticket.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    //User
    NICKNAME_ALREADY_EXISTS(409, "U001", "이미 존재하는 닉네임입니다."),
    USER_ALREADY_SIGNUP(409, "U002", "이미 가입한 사용자입니다."),

    //Global
    INPUT_VALUE_INVALID(400, "G001", "유효하지 않은 입력입니다."),
    HTTP_METHOD_NOT_ALLOWED(405, "G002", "지원하지 않는 HTTP 요청입니다."),
    INTERNAL_SERVER_ERROR(500, "G003", "내부 서버 에러입니다.");


    private final int status;
    private final String code;
    private final String message;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
