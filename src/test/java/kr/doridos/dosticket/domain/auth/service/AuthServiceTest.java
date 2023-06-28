package kr.doridos.dosticket.domain.auth.service;

import kr.doridos.dosticket.domain.auth.dto.SignInRequest;
import kr.doridos.dosticket.domain.auth.dto.SignInResponse;
import kr.doridos.dosticket.domain.auth.exception.SignInFailureException;
import kr.doridos.dosticket.domain.auth.support.jwt.JwtProvider;
import kr.doridos.dosticket.domain.user.EncodedPassword;
import kr.doridos.dosticket.domain.user.User;
import kr.doridos.dosticket.domain.user.UserType;
import kr.doridos.dosticket.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtProvider jwtProvider;

    private SignInRequest signInRequest;

    @BeforeEach
    public void setUp() {
        signInRequest = new SignInRequest("email@email.com", "123456a!");
    }

    @Test
    @DisplayName("패스워드와 이메일이 모두 일치하면 성공적으로 토큰을 발급한다.")
    void validUserSignInRequest_signIn_ReturnSignInResponse() {
        User user = new User(1L, "aaa@test.com", EncodedPassword.encode("123456a!"), "두루리루", "01012345432", UserType.USER, LocalDateTime.now(), LocalDateTime.now(), null);

        given(userRepository.findByEmail(signInRequest.getEmail())).willReturn(Optional.of(user));
        given(jwtProvider.createAccessToken("email@email.com", UserType.USER)).willReturn("accessToken");

        SignInResponse signInResponse = authService.signIn(signInRequest);

        assertThat(signInResponse.getToken()).isEqualTo("accessToken");
    }

    @Test
    @DisplayName("가입된 이메일이 존재하지 않아 exception 발생한다.")
    void notExistUserEmail_signIn_throwSignInFailureException() {
        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.signIn(signInRequest))
                .isInstanceOf(SignInFailureException.class)
                .hasMessage("로그인에 실패하였습니다.");

        then(userRepository).should().findByEmail(any());
    }

    @Test
    @DisplayName("패스워드가 일치하지 않으면 exception 발생한다.")
    void notExistUserPassword_signIn_throwSignInFailureException() {
        User user = new User(1L, "aaa@test.com", EncodedPassword.encode("123456a!1"), "두루리루", "01012345432", UserType.USER, LocalDateTime.now(), LocalDateTime.now(), null);

        given(userRepository.findByEmail(signInRequest.getEmail())).willReturn(Optional.of(user));

        assertThatThrownBy(() -> authService.signIn(signInRequest))
                .isInstanceOf(SignInFailureException.class)
                .hasMessage("로그인에 실패하였습니다.");
    }
}