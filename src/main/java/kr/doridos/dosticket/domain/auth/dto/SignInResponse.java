package kr.doridos.dosticket.domain.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignInResponse {

    private String token;

    public SignInResponse(final String token) {
        this.token = token;
    }
}
