package kr.doridos.dosticket.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.doridos.dosticket.domain.auth.dto.SignInRequest;
import kr.doridos.dosticket.domain.auth.service.AuthService;
import kr.doridos.dosticket.domain.user.UserType;
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

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class AuthControllerTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "123456a!";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;

    @BeforeEach
    void saveUser() {
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest(EMAIL, PASSWORD, "도리도스","01012341234", UserType.USER);
        userService.signUp(userSignUpRequest);
    }

    @Test
    @DisplayName("로그인에 성공한다.")
    public void user_signIn_success() throws Exception {
        SignInRequest signInRequest = new SignInRequest(EMAIL, PASSWORD);

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("token", notNullValue()))
                .andDo(document("userSignIn",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("이메일이 일치하지 않으면 로그인에 실패한다.")
    public void userNotExistsEmail_signIn_throw401() throws Exception {
        SignInRequest signInRequest = new SignInRequest("emjai1@email.com", PASSWORD);

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().is(ErrorCode.SIGN_IN_FAIL.getStatus()))
                .andExpect(jsonPath("$.message").value("로그인에 실패하였습니다."));
    }

    @Test
    @DisplayName("패스워드가 일치하지 않으면 로그인에 실패한다.")
    public void userNotMatchPassword_signIn_throw401() throws Exception {
        SignInRequest signInRequest = new SignInRequest("emjai1@email.com", PASSWORD);

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().is(ErrorCode.SIGN_IN_FAIL.getStatus()))
                .andExpect(jsonPath("$.message").value("로그인에 실패하였습니다."));
    }
}