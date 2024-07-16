package kr.doridos.dosticket.domain.user.service;

import kr.doridos.dosticket.domain.user.User;
import kr.doridos.dosticket.domain.user.UserType;
import kr.doridos.dosticket.domain.user.dto.NicknameRequest;
import kr.doridos.dosticket.domain.user.dto.UserInfoResponse;
import kr.doridos.dosticket.domain.user.dto.UserSignUpRequest;
import kr.doridos.dosticket.domain.user.exception.NicknameAlreadyExistsException;
import kr.doridos.dosticket.domain.user.exception.UserAlreadySignUpException;
import kr.doridos.dosticket.domain.user.repository.UserRepository;
import kr.doridos.dosticket.domain.user.fixture.UserFixture;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @DisplayName("회원가입을 진행한다")
    @Nested
    class Signup {
        @Test
        void 회원가입에_성공한다() {
            UserSignUpRequest userSignUpRequest = new UserSignUpRequest("test@test.com",
                    "12345678a!",
                    "test",
                    "01012341234",
                    UserType.USER);
            User user = UserFixture.일반_유저_생성();

            given(userRepository.save(any(User.class))).willReturn(user);

            long userId = userService.signUp(userSignUpRequest);

            assertThat(userId).isEqualTo(1L);
            then(userRepository).should().save(any(User.class));
        }

        @Test
        void 이미_존재하는_닉네임이면_예외가_발생한다() {
            //given
            User user = UserFixture.일반_유저_생성();
            UserSignUpRequest userSignUpRequest = new UserSignUpRequest("test1@test.com",
                    "12345678a!",
                    user.getNickname(),
                    "01012341234",
                    UserType.USER);

            given(userRepository.existsByNickname(user.getNickname())).willReturn(true);

            assertThatThrownBy(() -> userService.signUp(userSignUpRequest))
                    .isInstanceOf(NicknameAlreadyExistsException.class)
                    .hasMessage("이미 존재하는 닉네임입니다.");

            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        void 이미_존재하는_이메일이면_예외가_발생한다() {
            //given
            User user = UserFixture.일반_유저_생성();
            UserSignUpRequest userSignUpRequest = new UserSignUpRequest(user.getEmail(),
                    "12345678a!",
                    "하루하루",
                    "01012341234",
                    UserType.USER);

            given(userRepository.existsByEmail(user.getEmail())).willReturn(true);

            assertThatThrownBy(() -> userService.signUp(userSignUpRequest))
                    .isInstanceOf(UserAlreadySignUpException.class)
                    .hasMessage("이미 가입한 유저입니다.");

            verify(userRepository, never()).save(any(User.class));
        }
    }

    @DisplayName("유저 정보를 조회 및 변경한다")
    @Nested
    class UserInfo {

        @Test
        void 유저정보_조회에_성공한다() {
            //given
            User user = UserFixture.일반_유저_생성();
            //when
            UserInfoResponse userInfoResponse = userService.getUserInfo(user);
            //then
            assertSoftly(softly -> {
                softly.assertThat(userInfoResponse.getEmail()).isEqualTo(user.getEmail());
                softly.assertThat(userInfoResponse.getNickname()).isEqualTo(user.getNickname());
                softly.assertThat(userInfoResponse.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
            });
        }

        @Test
        void 변경하려는_닉네임이_같으면_예외를_발생한다() {
            //given
            NicknameRequest nicknameRequest = new NicknameRequest("test");
            User user = UserFixture.일반_유저_생성();

            given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
            //when, then
            assertThatThrownBy(() -> userService.updateNickname(nicknameRequest, user.getEmail()))
                    .isInstanceOf(NicknameAlreadyExistsException.class)
                    .hasMessage("이미 존재하는 닉네임입니다.");
        }

        @Test
        void 닉네임_변경에_성공한다() {
            NicknameRequest nicknameRequest = new NicknameRequest("test1");
            User user = UserFixture.일반_유저_생성();

            given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
            userService.updateNickname(nicknameRequest, user.getEmail());

            assertThat(user.getNickname()).isEqualTo(nicknameRequest.getNickname());
        }
    }
}


