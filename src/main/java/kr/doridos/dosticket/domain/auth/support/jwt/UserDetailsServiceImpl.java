package kr.doridos.dosticket.domain.auth.support.jwt;

import kr.doridos.dosticket.domain.user.entity.User;
import kr.doridos.dosticket.domain.user.exception.UserNotFoundException;
import kr.doridos.dosticket.domain.user.repository.UserRepository;
import kr.doridos.dosticket.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
        return new UserDetailsImpl(user, user.getEmail());
    }
}
