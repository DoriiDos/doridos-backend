package kr.doridos.dosticket.domain.user.controller;

import kr.doridos.dosticket.domain.auth.support.jwt.UserDetailsImpl;
import kr.doridos.dosticket.domain.user.dto.NicknameRequest;
import kr.doridos.dosticket.domain.user.dto.UserInfoResponse;
import kr.doridos.dosticket.domain.user.dto.UserSignUpRequest;
import kr.doridos.dosticket.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    public final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody @Valid final UserSignUpRequest userSignUpRequest) {
        final Long id = userService.signUp(userSignUpRequest);
        return ResponseEntity.created(URI.create("/users/me")).build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getMyInfo(@AuthenticationPrincipal final UserDetailsImpl userDetails) {
        final UserInfoResponse response = userService.getUserInfo(userDetails.getUser());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me/nickname")
    public ResponseEntity<Void> changeNickname(@RequestBody @Valid final NicknameRequest nicknameRequest,
                                               @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        userService.updateNickname(nicknameRequest, userDetails.getEmail());
        return ResponseEntity.noContent().build();
    }
}
