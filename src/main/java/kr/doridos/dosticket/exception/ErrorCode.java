package kr.doridos.dosticket.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    //User
    NICKNAME_ALREADY_EXISTS(409, "U001", "이미 존재하는 닉네임입니다."),
    USER_ALREADY_SIGNUP(409, "U002", "이미 가입한 유저입니다."),
    USER_NOT_FOUND(400, "U003", "유저가 존재하지 않습니다."),
    SIGN_IN_FAIL(401, "U004", "로그인에 실패하였습니다."),

    //Auth
    EXPIRED_AUTHORIZATION_TOKEN(400, "A001", "이미 만료된 토큰입니다."),
    INVALID_AUTHORIZATION_TOKEN(404, "A002", "유효하지 않은 토큰입니다."),
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
