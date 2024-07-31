package kr.doridos.dosticket.domain.auth.support.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.doridos.dosticket.exception.ErrorCode;
import kr.doridos.dosticket.exception.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        setErrorResponse(response, ErrorCode.TOKEN_NOT_FOUND.getMessage(), ErrorCode.TOKEN_NOT_FOUND);
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
