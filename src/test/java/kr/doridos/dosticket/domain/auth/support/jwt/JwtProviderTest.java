package kr.doridos.dosticket.domain.auth.support.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import kr.doridos.dosticket.domain.user.entity.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Key;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtProviderTest {

    @Autowired
    JwtProvider jwtProvider;

    @Value("${spring.jwt.secret-key}")
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Test
    @DisplayName("토큰을 만들기 위해 사용한 값과 토큰안에 담긴 값이 동일하다.")
    public void user_createAccessTokenByUser_parseClaimsWithSameValue() {
        String token = jwtProvider.createAccessToken("email@email.com", UserType.USER);
        Claims claims = parseClaims(token);

        assertThat(claims.getSubject()).isEqualTo("email@email.com");
        assertThat(claims.getSubject()).isNotEqualTo("email1@email.com");
        assertThat(claims.get("Role")).isEqualTo("USER");
    }
}