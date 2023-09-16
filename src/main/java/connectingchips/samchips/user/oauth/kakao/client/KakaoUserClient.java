package connectingchips.samchips.user.oauth.kakao.client;

import connectingchips.samchips.user.domain.SocialType;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.oauth.client.UserClient;
import connectingchips.samchips.user.oauth.kakao.config.KakaoOAuthConfig;
import connectingchips.samchips.user.oauth.kakao.dto.KakaoToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class KakaoUserClient implements UserClient {

    private final KakaoApiClient kakaoApiClient;
    private final KakaoOAuthConfig kakaoOAuthConfig;

    @Override
    public SocialType supportServer() {
        return null;
    }

    @Override
    public User fetch(String authCode) {
        KakaoToken tokenInfo = kakaoApiClient.fetchToken(tokenRequestParams(authCode));
        return null;
    }

    private MultiValueMap<String, String> tokenRequestParams(String authCode){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", kakaoOAuthConfig.clientId());
        params.add("redirect_uri", kakaoOAuthConfig.redirectUri());
        params.add("code", authCode);
        params.add("client_secret", kakaoOAuthConfig.clientSecret());
        return params;
    }
}
