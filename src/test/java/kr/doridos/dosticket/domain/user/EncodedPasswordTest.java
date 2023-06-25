package kr.doridos.dosticket.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kr.doridos.dosticket.domain.user.EncodedPassword.encode;
import static org.junit.jupiter.api.Assertions.*;

class EncodedPasswordTest {

    @Test
    @DisplayName("입력한 패스워드와 암호화된 패스워드가 일치한지 확인한다.")
    void signUp_encode_confirmPassword() {
        final String password = "1234567!a";
        final String encodePassword = encode(password);

        assertAll(
                () -> assertNotEquals(password, encodePassword),
                () -> assertTrue(EncodedPassword.matches(password),encodePassword)
        );
    }
}