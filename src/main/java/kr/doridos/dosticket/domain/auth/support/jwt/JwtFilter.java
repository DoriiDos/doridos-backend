package kr.doridos.dosticket.domain.auth.support.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.doridos.dosticket.domain.auth.exception.AuthenticationException;
import kr.doridos.dosticket.domain.auth.exception.InvalidTokenException;
import kr.doridos.dosticket.exception.ErrorCode;
import kr.doridos.dosticket.exception.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public JwtFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            final String token = jwtProvider.resolveToken(request);

            if (token != null && jwtProvider.validateToken(token)) {
                String email = jwtProvider.getUserEmail(token);
                setAuthentication(email);
            }

        } catch (AuthenticationException | InvalidTokenException e) {
            setErrorResponse(response, e.getMessage(), e.getErrorCode());
            return;
        }
        filterChain.doFilter(request, response);
    }

    public void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtProvider.getAuthentication(email);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    public void setErrorResponse(HttpServletResponse response, String message, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
