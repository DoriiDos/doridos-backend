package kr.doridos.dosticket.domain.user.controller;

import kr.doridos.dosticket.domain.user.dto.UserSignUpRequest;
import kr.doridos.dosticket.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.created(URI.create("/users/" + id)).build();
    }
}
