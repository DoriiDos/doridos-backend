package kr.doridos.dosticket.domain.auth.oauth;

import kr.doridos.dosticket.domain.user.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collections;
import java.util.Map;

public class CustomOAuth2User extends DefaultOAuth2User {

    private final User socialUser;

    public CustomOAuth2User(User socialUser, Map<String, Object> attributes, String nameAttributeKey) {
        super(Collections.singletonList(new SimpleGrantedAuthority(socialUser.getUserType().getAuthority())), attributes, nameAttributeKey);
        this.socialUser = socialUser;
    }

    public User getSocialUser() {
        return socialUser;
    }
}
