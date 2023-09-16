package connectingchips.samchips.user.oauth.kakao.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.kakao")
public record KakaoOAuthConfig (
        String clientId,
        String clientSecret,
        String redirectUri,
        String[] scope
) {
}
