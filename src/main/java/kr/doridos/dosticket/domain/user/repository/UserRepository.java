package kr.doridos.dosticket.domain.user.repository;

import kr.doridos.dosticket.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(final String email);

    boolean existsByNickname(final String nickname);

    Optional<User> findByEmail(final String email);
}
