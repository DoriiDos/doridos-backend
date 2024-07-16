package kr.doridos.dosticket.domain.user.fixture;
import kr.doridos.dosticket.domain.user.User;
import kr.doridos.dosticket.domain.user.UserType;
import kr.doridos.dosticket.domain.user.dto.UserSignUpRequest;

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
                .id(2L)
                .email("admin@test.com")
                .password("12345678a!")
                .nickname("admin")
                .phoneNumber("01012344321")
                .userType(UserType.TICKET_MANAGER)
                .build();
    }

    public static UserSignUpRequest 일반_생성_요청() {
        return new UserSignUpRequest(
                UserFixture.일반_유저_생성().getEmail(),
                UserFixture.일반_유저_생성().getPassword(),
                UserFixture.일반_유저_생성().getNickname(),
                UserFixture.일반_유저_생성().getPhoneNumber(),
                UserFixture.일반_유저_생성().getUserType()
        );
    }

    public static UserSignUpRequest 관리자_생성_요청() {
        return new UserSignUpRequest(
                UserFixture.관리자_생성().getEmail(),
                UserFixture.관리자_생성().getPassword(),
                UserFixture.관리자_생성().getNickname(),
                UserFixture.관리자_생성().getPhoneNumber(),
                UserFixture.관리자_생성().getUserType()
        );
    }
}

