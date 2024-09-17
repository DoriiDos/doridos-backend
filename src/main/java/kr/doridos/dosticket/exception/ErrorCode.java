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
    TOKEN_NOT_FOUND(400, "A003", "토큰이 존재하지 않습니다."),
    FORBIDDEN_USER(400, "A004" , "권한이 없는 유저입니다."),
    NOT_SUPPORT_OAUTH_CLIENT(400,"A005" ,"지원하지 않는 OAuth2입니다."),
    //Global
    INPUT_VALUE_INVALID(400, "G001", "유효하지 않은 입력입니다."),
    HTTP_METHOD_NOT_ALLOWED(405, "G002", "지원하지 않는 HTTP 요청입니다."),
    INTERNAL_SERVER_ERROR(500, "G003", "내부 서버 에러입니다."),
    //Ticket
    NOT_TICKET_MANAGER(401, "T001", "권한이 없는 사용자입니다."),
    CATEGORY_NOT_FOUND(400, "T002", "카테고리가 존재하지 않습니다."),
    SEAT_NOT_FOUND(400,"T003" , "좌석이 존재하지 않습니다."),
    PLACE_NOT_FOUND(400, "T004" , "장소가 존재하지 않습니다."),
    DATE_NOT_CORRECT(400, "T005", "시작일은 종료일 이후가 될 수 없습니다."),
    TICKET_NOT_FOUND(400, "T006", "티켓을 찾을 수 없습니다."),
    CATEGORY_EXIST(400, "T008", "카테고리가 이미 존재합니다."),

    //Schedule
    SCHEDULE_ALREADY_EXIST(400,"S001" , "해당시간에 이미 스케줄이 존재합니다."),
    RESERVATION_NOT_START(400,"T009" ,"예매가 아직 시작되지 않았습니다."),
    SCHEDULE_NOT_FOUND(400, "T010" , "스케줄이 존재하지 않습니다."),
    SEAT_ALREADY_RESERVED(400,"T111" , "이미 예약된 좌석입니다."),
    LOCK_ACQUISITION_FAILED(400,"L001" ,"LOCK 획득에 실패했습니다."),
    LOCK_INTERRUPTED(400, "L002" ,"LOCK Interrupted exception" ),

    RESERVATION_NOT_FOUND(400, "R001" , "예매가 존재하지 않습니다."),
    RESERVATION_NOT_OWNER(401,"R002" , "해당 예매 내역에 접근할 수 없습니다."),
    PAYMENT_NOT_FOUND(400,"P001", "결제내역이 존재하지 않습니다.");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
