package kr.doridos.dosticket.domain.user.dto;

import kr.doridos.dosticket.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoResponse {

    private String email;
    private String nickname;
    private String phoneNumber;

    public UserInfoResponse(final String email, final String nickname, final String phoneNumber) {
        this.email = email;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }

    public static UserInfoResponse of(final User user) {
        return new UserInfoResponse(
                user.getEmail(),
                user.getNickname(),
                user.getPhoneNumber()
        );
    }
}
