package kr.doridos.dosticket.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.doridos.dosticket.domain.auth.dto.SignInRequest;
import kr.doridos.dosticket.domain.auth.service.AuthService;
import kr.doridos.dosticket.domain.user.User;
import kr.doridos.dosticket.domain.user.dto.UserSignUpRequest;
import kr.doridos.dosticket.domain.user.service.UserService;
import kr.doridos.dosticket.domain.user.util.UserFixture;
import kr.doridos.dosticket.exception.ErrorCode;
import org.junit.jupiter.api.*;
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

@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class AuthControllerTest {

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
        User user = UserFixture.일반_유저_생성();

        UserSignUpRequest userSignUpRequest = new UserSignUpRequest(
                user.getEmail(),
                user.getPassword(),
                user.getNickname(),
                user.getPhoneNumber(),
                user.getUserType());
        userService.signUp(userSignUpRequest);
    }

    @Test
    void 로그인에_성공한다() throws Exception {
        SignInRequest signInRequest = new SignInRequest("test@test.com", "12345678a!");

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("token", notNullValue()))
                .andDo(document("유저 로그인 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    void 이메일이_다르면_로그인에_실패한다401() throws Exception {
        SignInRequest signInRequest = new SignInRequest("test1@test.com", "12345678a!");

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().is(ErrorCode.SIGN_IN_FAIL.getStatus()))
                .andExpect(jsonPath("$.message").value("로그인에 실패하였습니다."));
    }

    @Test
    void 패스워드가_일치하지_않으면_로그인에_실패한다401() throws Exception {
        SignInRequest signInRequest = new SignInRequest("test@test.com", "12345678aa!");

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().is(ErrorCode.SIGN_IN_FAIL.getStatus()))
                .andExpect(jsonPath("$.message").value("로그인에 실패하였습니다."));
    }
}