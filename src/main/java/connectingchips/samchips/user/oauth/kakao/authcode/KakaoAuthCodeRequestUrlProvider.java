package connectingchips.samchips.user.oauth.kakao.authcode;

import connectingchips.samchips.user.domain.SocialType;
import connectingchips.samchips.user.oauth.authcode.AuthCodeRequestUrlProvider;
import connectingchips.samchips.user.oauth.kakao.config.KakaoOAuthConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class KakaoAuthCodeRequestUrlProvider implements AuthCodeRequestUrlProvider {

    private final KakaoOAuthConfig kakaoOAuthConfig;

    @Override
    public SocialType supportServer() {
        return SocialType.KAKAO;
    }

    @Override
    public String provideUrl() {
        return UriComponentsBuilder
                .fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", kakaoOAuthConfig.clientId())
                .queryParam("redirect_uri", kakaoOAuthConfig.redirectUri())
                .queryParam("scope", String.join(",", kakaoOAuthConfig.scope()))
                .toUriString();
    }
}
