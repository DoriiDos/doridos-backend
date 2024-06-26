package kr.doridos.dosticket.domain.auth.service;

import kr.doridos.dosticket.domain.auth.oauth.CustomOAuth2User;
import kr.doridos.dosticket.domain.auth.oauth.OauthAttributes;
import kr.doridos.dosticket.domain.auth.oauth.UserProfile;
import kr.doridos.dosticket.domain.auth.support.jwt.JwtProvider;
import kr.doridos.dosticket.domain.user.User;
import kr.doridos.dosticket.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        UserProfile userProfile = OauthAttributes.extract(registrationId, attributes);
        User socialUser = saveOrUpdateUserProfile(userProfile);

        return new CustomOAuth2User(socialUser, attributes, userNameAttributeName);
    }

    private User saveOrUpdateUserProfile(UserProfile userProfile) {
        return userRepository.findByEmail(userProfile.getEmail())
                .orElseGet(() -> userRepository.save(userProfile.toEntity()));
    }
}
