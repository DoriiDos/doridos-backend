package kr.doridos.dosticket.domain.auth.oauth;

import kr.doridos.dosticket.domain.auth.support.jwt.JwtProvider;
import kr.doridos.dosticket.domain.user.entity.User;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User socialUser = ((CustomOAuth2User) authentication.getPrincipal()).getSocialUser();
        String token = jwtProvider.createAccessToken(socialUser.getEmail(), socialUser.getUserType());

        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        response.setStatus(HttpStatus.OK.value());
    }
}
