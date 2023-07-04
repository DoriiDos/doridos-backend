package kr.doridos.dosticket.domain.user.service;

import kr.doridos.dosticket.domain.user.User;
import kr.doridos.dosticket.domain.user.dto.NicknameRequest;
import kr.doridos.dosticket.domain.user.dto.UserInfoResponse;
import kr.doridos.dosticket.domain.user.dto.UserSignUpRequest;
import kr.doridos.dosticket.domain.user.exception.NicknameAlreadyExistsException;
import kr.doridos.dosticket.domain.user.exception.UserAlreadySignUpException;
import kr.doridos.dosticket.domain.user.exception.UserNotFoundException;
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

    public UserInfoResponse getUserInfo(final User user) {
        return UserInfoResponse.of(user);
    }

    public void updateNickname(final NicknameRequest request, final String email) {
        User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> { throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND); });
        validateUpdateByNickname(request.getNickname(), user.getNickname());
        user.updateNickname(request.getNickname());
    }

    private void validateDuplicateByEmail(final String email) {
        if (userRepository.existsByEmail(email))
            throw new UserAlreadySignUpException(ErrorCode.USER_ALREADY_SIGNUP);
    }

    private void validateDuplicateByNickname(final String nickname) {
        if (userRepository.existsByNickname(nickname))
            throw new NicknameAlreadyExistsException(ErrorCode.NICKNAME_ALREADY_EXISTS);
    }

    private void validateUpdateByNickname(final String nickname, final String updateNickname) {
        if(nickname.equals(updateNickname))
            throw new NicknameAlreadyExistsException(ErrorCode.NICKNAME_ALREADY_EXISTS);
    }
}

