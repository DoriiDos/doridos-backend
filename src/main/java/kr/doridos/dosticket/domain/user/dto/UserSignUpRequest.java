package kr.doridos.dosticket.domain.user.dto;

import kr.doridos.dosticket.domain.user.EncodedPassword;
import kr.doridos.dosticket.domain.user.User;
import kr.doridos.dosticket.domain.user.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class UserSignUpRequest {

    @Email(message = "이메일 형식에 맞지 않습니다.")
    @NotBlank(message = "email은 빈값일 수 없습니다.")
    private String email;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    @NotBlank(message = "password은 빈값일 수 없습니다.")
    private String password;

    @Length(min = 2, max = 8, message = "nickname은 2자 이상 8자 이하로 입력하세요. ")
    @NotBlank(message = "nickname은 빈값일 수 없습니다.")
    private String nickname;

    @Pattern(regexp = "(01[016789])(\\d{3,4})(\\d{4})", message = "-을 제외하고 입력하세요.")
    @NotBlank(message = "phoneNumber은 빈값일 수 없습니다.")
    private String phoneNumber;

    private UserType userType;

    public UserSignUpRequest(final String email, final String password, final String nickname,
                             final String phoneNumber, final UserType userType) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
    }

    public User toEntity() {
        return User.of(email, EncodedPassword.encode(password), nickname, phoneNumber, userType);
    }
}
