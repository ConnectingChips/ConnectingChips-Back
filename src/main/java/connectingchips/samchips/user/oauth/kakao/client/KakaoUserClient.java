package connectingchips.samchips.user.oauth.kakao.client;

import connectingchips.samchips.user.domain.SocialType;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.oauth.client.UserClient;
import connectingchips.samchips.user.oauth.kakao.config.KakaoOAuthConfig;
import connectingchips.samchips.user.oauth.kakao.dto.KakaoToken;
import connectingchips.samchips.user.oauth.kakao.dto.KakaoUserResponse;
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
        return SocialType.KAKAO;
    }

    @Override
    public User fetch(String authCode) {
        // 1. Auth Code를 통해서 AccessToken을 조회
        KakaoToken tokenInfo = kakaoApiClient.fetchToken(tokenRequestParams(authCode));
        // 2. AccessToken으로 회원 정보 조회
        KakaoUserResponse kakaoUserResponse = kakaoApiClient.fetchUser("Bearer " + tokenInfo.accessToken());
        // 3. 회원 정보를 User 객체로 변환
        return kakaoUserResponse.toEntity();
    }

    private MultiValueMap<String, String> tokenRequestParams(String authCode){
        /**
         * AccessToken 받기 API의 Request에 사용되는 파라미터 설정
         * grant_type - refresh_token으로 고정
         * client_id - 앱 REST API 키
         * redirect_uri - 인가 코드가 리다이렉트된 URI
         * code - 인가 코드 받기 요청으로 얻은 인가 코드
         * client_secret - 토큰 발급 시, 보안을 강화하기 위해 추가 확인하는 코드, ON 상태인 경우 필수 설정해야 함
         */

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoOAuthConfig.clientId());
        params.add("redirect_uri", kakaoOAuthConfig.redirectUri());
        params.add("code", authCode);
        params.add("client_secret", kakaoOAuthConfig.clientSecret());
        return params;
    }
}
