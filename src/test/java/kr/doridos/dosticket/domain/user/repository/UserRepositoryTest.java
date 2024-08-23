package kr.doridos.dosticket.domain.user.repository;

import kr.doridos.dosticket.config.JpaAuditingConfig;
import kr.doridos.dosticket.config.QueryDslConfig;
import kr.doridos.dosticket.domain.user.entity.User;
import kr.doridos.dosticket.domain.user.fixture.UserFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaAuditingConfig.class, QueryDslConfig.class})
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private User user;

    @BeforeEach
    void setup() {
        user = UserFixture.일반_유저_생성();
        userRepository.save(user);
    }

    @DisplayName("회원 정보를 저장한다.")
    @Test
    void user_signup() {
        //given
        User createUser = UserFixture.일반_유저_생성();
        //when
        User savedUser = userRepository.save(createUser);
        //then
        User user = userRepository.getReferenceById(savedUser.getId());
        assertThat(user).isSameAs(savedUser);
    }

    @Test
    @DisplayName("사용자 이메일이 존재하는지 확인한다.")
    void existsUserByEmail() {
        boolean existEmail = userRepository.existsByEmail(user.getEmail());
        assertThat(existEmail).isTrue();
    }

    @Test
    @DisplayName("사용자 닉네임이 존재하는지 확인한다.")
    void existsUserByNickname() {
        boolean existNickname = userRepository.existsByNickname(user.getNickname());
        assertThat(existNickname).isTrue();
    }
}