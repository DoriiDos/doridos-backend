package kr.doridos.dosticket.domain.auth.oauth;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public enum OauthAttributes {

    GOOGLE("google", attributes -> new UserProfile(
            (String) attributes.get("email"),
            "Social" + UUID.randomUUID()
    )),

    NAVER("naver", attributes -> {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return new UserProfile(
                (String) response.get("email"),
                (String) response.get("nickname")
        );
    });

    private final String registrationId;
    private final Function<Map<String, Object>, UserProfile> userProfileFactory;

    OauthAttributes(String registrationId, Function<Map<String, Object>, UserProfile> userProfileFactory) {
        this.registrationId = registrationId;
        this.userProfileFactory = userProfileFactory;
    }

    public static UserProfile extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(provider -> registrationId.equals(provider.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .userProfileFactory.apply(attributes);
    }
}
