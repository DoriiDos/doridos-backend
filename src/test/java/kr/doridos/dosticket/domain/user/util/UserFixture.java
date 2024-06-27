package kr.doridos.dosticket.domain.user.util;

import kr.doridos.dosticket.domain.user.EncodedPassword;
import kr.doridos.dosticket.domain.user.User;
import kr.doridos.dosticket.domain.user.UserType;

public class UserFixture {

    public static User 일반_유저_생성() {
        return User.builder()
                .id(1L)
                .email("test@test.com")
                .password("12345678a!")
                .nickname("test")
                .phoneNumber("01012341234")
                .userType(UserType.USER)
                .build();
    }

    public static User 관리자_생성() {
        return User.builder()
                .email("admin@test.com")
                .password("12345678a!")
                .nickname("admin")
                .phoneNumber("01012344321")
                .userType(UserType.TICKET_MANAGER)
                .build();
    }
}

