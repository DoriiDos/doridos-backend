package kr.doridos.dosticket.domain.auth.service;

import kr.doridos.dosticket.domain.auth.exception.OAuthNotSupportException;
import kr.doridos.dosticket.domain.auth.oauth.CustomOAuth2User;
import kr.doridos.dosticket.domain.auth.oauth.OauthAttributes;
import kr.doridos.dosticket.domain.auth.oauth.UserProfile;
import kr.doridos.dosticket.domain.user.entity.User;
import kr.doridos.dosticket.domain.user.entity.UserType;
import kr.doridos.dosticket.exception.ErrorCode;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class CustomOAuthTest {

    @Test
    void CustomOAuth2User_생성_및_속성을_확인한다() {
        User user = new User(
                1L, "test@example.com", null, "nickname", "01012345678",
                UserType.USER, LocalDateTime.now(), LocalDateTime.now(), null);
        Map<String, Object> attributes = Map.of("email", "test@example.com");
        String nameAttributeKey = "email";

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(user, attributes, nameAttributeKey);

        assertThat(customOAuth2User.getAttributes()).isEqualTo(attributes);
        assertThat(customOAuth2User.getName()).isEqualTo("test@example.com");
        assertThat(customOAuth2User.getSocialUser()).isEqualTo(user);
    }

    @Test
    void Google_OAuth_로그인시_정보_추출() {
        Map<String, Object> googleAttributes = Map.of("email", "test@example.com");

        UserProfile userProfile = OauthAttributes.extract("google", googleAttributes);

        assertThat(userProfile.getEmail()).isEqualTo("test@example.com");
        assertThat(userProfile.getNickname()).startsWith("Social");
    }

    @Test
    void Naver_OAuth_로그인시_정보_추출() {
        Map<String, Object> naverAttributes = Map.of("response", Map.of("email", "test@naver.com", "nickname", "naverUser"));

        UserProfile userProfile = OauthAttributes.extract("naver", naverAttributes);

        assertThat(userProfile.getEmail()).isEqualTo("test@naver.com");
        assertThat(userProfile.getNickname()).isEqualTo("naverUser");
    }

    @Test
    void 지원하지_않는_OAuth_제공자_정보_추출시_예외가_발생한다() {
        Map<String, Object> unknownAttributes = Map.of("email", "test@unknown.com");

        assertThatThrownBy(() -> OauthAttributes.extract("kakaooo", unknownAttributes))
                .isInstanceOf(OAuthNotSupportException.class)
                .hasMessage(ErrorCode.NOT_SUPPORT_OAUTH_CLIENT.getMessage());
    }
}


