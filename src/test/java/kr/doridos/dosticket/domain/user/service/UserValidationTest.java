package kr.doridos.dosticket.domain.user.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import kr.doridos.dosticket.domain.user.dto.NicknameRequest;
import kr.doridos.dosticket.domain.user.dto.UserSignUpRequest;
import kr.doridos.dosticket.domain.user.entity.UserType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class UserValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @ParameterizedTest
    @ValueSource(strings = {"emiamail", "emam1234", "emailemail.com"})
    void 회원가입시_잘못된이메일_형식이면_예외가_발생한다(String email) {
        UserSignUpRequest request = new UserSignUpRequest(
                email,
                "1234aA5678!",
                "nickname",
                "01012341234",
                UserType.USER
        );

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<UserSignUpRequest>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("이메일 형식에 맞지 않습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {""})
    void 회원가입시_빈값이면_예외가_발생한다(String email) {
        UserSignUpRequest request = new UserSignUpRequest(
                email,
                "1234aA5678!",
                "nickname",
                "01012341234",
                UserType.USER
        );

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<UserSignUpRequest>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("email은 빈값일 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234567", "123", "12345!2", "1234567a"})
    void 회원가입시_비밀번호_형식이_맞지않으면_예외가_발생한다(String password) {
        UserSignUpRequest request = new UserSignUpRequest(
                "email@naver.com",
                password,
                "nickname",
                "01012341234",
                UserType.USER
        );

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<UserSignUpRequest>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"01121", "012345", "1234", "010-1234-1234"})
    void 회원가입시_핸드폰번호_형식이_맞지않으면_예외가_발생한다(String phoneNumber) {
        UserSignUpRequest request = new UserSignUpRequest(
                "email@naver.com",
                "123456a!",
                "nickname",
                phoneNumber,
                UserType.USER
        );

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<UserSignUpRequest>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("-을 제외하고 입력하세요.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"ㅎ", "안녕하세요8글자넘어보겠습니다."})
    void 회원가입시_닉네임_형식이_맞지않으면_예외가_발생한다(String nickname) {
        UserSignUpRequest request = new UserSignUpRequest(
                "email@naver.com",
                "123456a!",
                nickname,
                "01012341234",
                UserType.USER
        );

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<UserSignUpRequest>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("nickname은 2자 이상 8자 이하로 입력하세요.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"ㅎ", "안녕하세요8글자넘어보겠습니다."})
    void 닉네임_변경시_형식이_맞지않으면_예외가_발생한다(String nickname) {
        NicknameRequest request = new NicknameRequest(nickname);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<NicknameRequest>> constraintViolations = validator.validate(request);

        assertThat(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("nickname은 2자 이상 8자 이하로 입력하세요.");
    }
}
