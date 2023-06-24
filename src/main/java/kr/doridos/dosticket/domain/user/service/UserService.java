package kr.doridos.dosticket.domain.user.service;

import kr.doridos.dosticket.domain.user.User;
import kr.doridos.dosticket.domain.user.dto.UserSignUpRequest;
import kr.doridos.dosticket.domain.user.exception.NicknameAlreadyExistsException;
import kr.doridos.dosticket.domain.user.exception.UserAlreadySignUpException;
import kr.doridos.dosticket.domain.user.repository.UserRepository;
import kr.doridos.dosticket.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long signUp(final UserSignUpRequest userSignUpRequest) {
        validateDuplicateByEmail(userSignUpRequest.getEmail());
        validateDuplicateByNickname(userSignUpRequest.getNickname());

        final User savedUser = userRepository.save(userSignUpRequest.toEntity());
        return savedUser.getId();
    }

    private void validateDuplicateByEmail(final String email) {
        if (userRepository.existsByEmail(email))
            throw new UserAlreadySignUpException(ErrorCode.USER_ALREADY_SIGNUP);
    }

    private void validateDuplicateByNickname(final String nickname) {
        if (userRepository.existsByNickname(nickname))
            throw new NicknameAlreadyExistsException(ErrorCode.NICKNAME_ALREADY_EXISTS);
    }
}

