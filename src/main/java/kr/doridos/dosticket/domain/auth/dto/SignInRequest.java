package kr.doridos.dosticket.domain.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignInRequest {

    private String email;
    private String password;

    public SignInRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }
}
