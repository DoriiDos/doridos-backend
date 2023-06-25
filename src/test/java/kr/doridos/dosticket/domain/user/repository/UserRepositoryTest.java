package kr.doridos.dosticket.domain.user.repository;

import kr.doridos.dosticket.config.JpaAuditingConfig;
import kr.doridos.dosticket.domain.user.User;
import kr.doridos.dosticket.domain.user.UserType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaAuditingConfig.class)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private String setUpEmail;
    private String setUpNickname;

    @BeforeEach
    void setup() {
        setUpEmail = "example22@test.com";
        String password = "123456!a";
        setUpNickname = "도리도스";
        String phoneNumber = "01012345678";

        userRepository.save(User.of(setUpEmail, password, setUpNickname, phoneNumber, UserType.USER));
    }

    @DisplayName("회원 정보를 저장한다.")
    @Test
    void user_signup() {
        //given
        String email = "example@test.com";
        String password = "123456!a";
        String nickname = "hahaha";
        String phoneNumber = "01012345678";

        //when
        User savedUser = userRepository.save(
                User.of(email, password, nickname, phoneNumber, UserType.USER));

        //then
        User user = userRepository.getReferenceById(savedUser.getId());
        assertThat(user).isSameAs(savedUser);
    }

    @Test
    @DisplayName("사용자 이메일이 존재하는지 확인한다.")
    void existsUserByEmail() {
        final boolean existEmail = userRepository.existsByEmail(setUpEmail);
        assertThat(existEmail).isTrue();
    }

    @Test
    @DisplayName("사용자 닉네임이 존재하는지 확인한다.")
    void existsUserByNickname() {
        final boolean existNickname = userRepository.existsByNickname(setUpNickname);
        assertThat(existNickname).isTrue();
    }
}