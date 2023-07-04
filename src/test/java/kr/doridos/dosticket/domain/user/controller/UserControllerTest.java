package kr.doridos.dosticket.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.doridos.dosticket.domain.auth.dto.SignInRequest;
import kr.doridos.dosticket.domain.auth.service.AuthService;
import kr.doridos.dosticket.domain.user.UserType;
import kr.doridos.dosticket.domain.user.dto.NicknameRequest;
import kr.doridos.dosticket.domain.user.dto.UserSignUpRequest;
import kr.doridos.dosticket.domain.user.service.UserService;
import kr.doridos.dosticket.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class UserControllerTest {

    private static final String EMAIL = "email@test.com";
    private static final String PASSWORD = "123456a!";
    private static final String NICK_NAME = "도리도스";
    private static final String PHONE_NUMBER = "01012341234";
    private static final UserType USER_TYPE = UserType.USER;
    private String token;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    @BeforeEach
    void setUp() {
        final UserSignUpRequest userSignUpRequest = new UserSignUpRequest(EMAIL, PASSWORD, NICK_NAME, PHONE_NUMBER, USER_TYPE);
        userService.signUp(userSignUpRequest);

        final SignInRequest signInRequest = new SignInRequest(EMAIL, PASSWORD);
        token = authService.signIn(signInRequest).getToken();
    }

    @Test
    @DisplayName("회원가입에 성공한다.")
    void signUp_success() throws Exception {
        final UserSignUpRequest userSignUpRequest = new UserSignUpRequest("test@email.com", PASSWORD, "도도도도", PHONE_NUMBER, USER_TYPE);

        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSignUpRequest)))
                .andExpect(status().isCreated())
                .andDo(document("userSignUp",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("유저 정보조회에 성공한다")
    void success_getUserInfo() throws Exception {

        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(document("getUserInfo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("유저 닉네임 변경에 성공한다.")
    void success_updateUserNickname() throws Exception {
        NicknameRequest nicknameRequest = new NicknameRequest("도리도도");

        mockMvc.perform(patch("/users/me/nickname")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nicknameRequest)))
                .andExpect(status().isNoContent())
                .andDo(document("updateUserNickname",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("기존 닉네임과 동일한 닉네임으로 변경 요청시 에러가 발생한다.")
    void updateUserNickname_sameNickname_throwException409() throws Exception {
        NicknameRequest nicknameRequest = new NicknameRequest("도리도스");

        mockMvc.perform(patch("/users/me/nickname")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nicknameRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("이미 존재하는 닉네임입니다."));
    }
}
