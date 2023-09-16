package connectingchips.samchips.user.oauth.kakao.client;

import connectingchips.samchips.user.oauth.kakao.dto.KakaoToken;
import connectingchips.samchips.user.oauth.kakao.dto.KakaoUserResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

/**
 * Http Interface Client 사용
 * Url에는 AccessToken을 받아오기 위한 URL
 * ContentType과 요청 파라미터를 받아야 하므로 MultiValueMap을 파라미터로 설정
 */
public interface KakaoApiClient {

    // AuthCode로 AccessToken 받아오기
    @PostExchange(url = "https://kauth.kakao.com/oauth/token", contentType = APPLICATION_FORM_URLENCODED_VALUE)
    KakaoToken fetchToken(@RequestParam MultiValueMap<String, String> params);

    // AccessToken을 통해 회원 정보 조회
    @GetExchange("https://kapi.kakao.com/v2/user/me")
    KakaoUserResponse fetchUser(@RequestHeader(name = AUTHORIZATION) String bearerToken);
}
