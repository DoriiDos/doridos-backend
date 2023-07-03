package kr.doridos.dosticket.domain.user.service;

import kr.doridos.dosticket.domain.user.User;
import kr.doridos.dosticket.domain.user.UserType;
import kr.doridos.dosticket.domain.user.dto.UserInfoResponse;
import kr.doridos.dosticket.domain.user.dto.UserSignUpRequest;
import kr.doridos.dosticket.domain.user.exception.NicknameAlreadyExistsException;
import kr.doridos.dosticket.domain.user.exception.UserAlreadySignUpException;
import kr.doridos.dosticket.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("회원가입에 성공한다.")
    void signUp_success() {
        final UserSignUpRequest userSignUpRequest = new UserSignUpRequest("aaa@test.com", "123456a!", "두루리루", "01012345432", UserType.USER);
        User user = new User(1L, "aaa@test.com", "123456a!", "두루리루", "01012345432", UserType.USER, LocalDateTime.now(), LocalDateTime.now(), null);

        given(userRepository.save(any(User.class))).willReturn(user);

        long userId = userService.signUp(userSignUpRequest);

        assertThat(userId).isEqualTo(1L);
        then(userRepository).should().save(any(User.class));
    }

    @Test
    @DisplayName("회원가입시 이미 존재하는 닉네임으로 가입을 시도하면 예외가 발생한다.")
    void signUp_duplicatedNickname_throwException() {
        final String nickname = "도리도스";
        final UserSignUpRequest userSignUpRequest = new UserSignUpRequest("aaa@test.com", "1234567!", nickname, "01012345432", UserType.USER);

        given(userRepository.existsByNickname(nickname)).willReturn(true);

        assertThatThrownBy(() -> userService.signUp(userSignUpRequest))
                .isInstanceOf(NicknameAlreadyExistsException.class);

        then(userRepository).should().existsByNickname(nickname);
    }

    @Test
    @DisplayName("회원가입시 이미 가입된 이메일로 가입을 시도하면 예외가 발생한다.")
    void signUp_duplicatedEmail_throwException() {
        final String email = "example@test.com";
        final UserSignUpRequest userSignUpRequest = new UserSignUpRequest(email, "1234567a!", "두리두스", "01012345432", UserType.USER);

        given(userRepository.existsByEmail(email)).willReturn(true);

        assertThatThrownBy(() -> userService.signUp(userSignUpRequest))
                .isInstanceOf(UserAlreadySignUpException.class);

        then(userRepository).should().existsByEmail(email);
    }

    @Test
    @DisplayName("유저의 정보 조회에 성공한다.")
    void user_getInfo_success() {
       //given
        User user = new User("email@email.com","hahaha", "01012341234");

        //when
        UserInfoResponse userInfoResponse = userService.getUserInfo(user);

        //then
        assertAll(
                () -> assertThat(userInfoResponse.getEmail()).isEqualTo(user.getEmail()),
                () -> assertThat(userInfoResponse.getNickname()).isEqualTo(user.getNickname()),
                () -> assertThat(userInfoResponse.getPhoneNumber()).isEqualTo(user.getPhoneNumber()));
    }
}