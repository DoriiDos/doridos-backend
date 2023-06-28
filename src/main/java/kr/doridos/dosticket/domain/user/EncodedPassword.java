package kr.doridos.dosticket.domain.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EncodedPassword {

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String encode(final String password) {
        return encoder.encode(password);
    }

    public static boolean matches(String rawPassword, String encodePassword) {
        return encoder.matches(rawPassword, encodePassword);
    }
}
