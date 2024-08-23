package kr.doridos.dosticket.domain.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class NicknameRequest {

    @Length(min = 2, max = 8, message = "nickname은 2자 이상 8자 이하로 입력하세요.")
    @NotBlank(message = "nickname은 빈값일 수 없습니다.")
    private String nickname;

    public NicknameRequest(final String nickname) {
        this.nickname = nickname;
    }
}
