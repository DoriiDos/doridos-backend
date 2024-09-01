package kr.doridos.dosticket.domain.auth.support.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import kr.doridos.dosticket.domain.auth.exception.AuthenticationException;
import kr.doridos.dosticket.domain.auth.exception.InvalidTokenException;
import kr.doridos.dosticket.domain.user.entity.UserType;
import kr.doridos.dosticket.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${spring.jwt.secret-key}")
    private String secretKey;

    @Value("${spring.jwt.expire-length}")
    private Long validityInMilliseconds;

    private Key key;

    private final UserDetailsServiceImpl userDetailsService;
    public static final String ACCESS_TOKEN_HEADER_NAME = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_KEY = "Role";

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createAccessToken(String email, UserType type) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(email)
                .claim(AUTHORIZATION_KEY, type)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validityInMilliseconds))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserEmail(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(ACCESS_TOKEN_HEADER_NAME);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new AuthenticationException(ErrorCode.EXPIRED_AUTHORIZATION_TOKEN);
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException(ErrorCode.INVALID_AUTHORIZATION_TOKEN);
        }
    }
}
