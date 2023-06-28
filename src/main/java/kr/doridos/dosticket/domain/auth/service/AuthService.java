package kr.doridos.dosticket.domain.auth.service;

import kr.doridos.dosticket.domain.auth.dto.SignInRequest;
import kr.doridos.dosticket.domain.auth.dto.SignInResponse;
import kr.doridos.dosticket.domain.auth.exception.SignInFailureException;
import kr.doridos.dosticket.domain.auth.support.jwt.JwtProvider;
import kr.doridos.dosticket.domain.user.EncodedPassword;
import kr.doridos.dosticket.domain.user.User;
import kr.doridos.dosticket.domain.user.repository.UserRepository;
import kr.doridos.dosticket.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public AuthService(final UserRepository userRepository, final JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    public SignInResponse signIn(final SignInRequest signInRequest) {
        final User user = userRepository.findByEmail(signInRequest.getEmail())
                .orElseThrow(() -> { throw new SignInFailureException(ErrorCode.SIGN_IN_FAIL); });

        if (!EncodedPassword.matches(signInRequest.getPassword(), user.getPassword()))
            throw new SignInFailureException(ErrorCode.SIGN_IN_FAIL);

        String token = jwtProvider.createAccessToken(signInRequest.getEmail(), user.getUserType());
        return new SignInResponse(token);
    }
}
