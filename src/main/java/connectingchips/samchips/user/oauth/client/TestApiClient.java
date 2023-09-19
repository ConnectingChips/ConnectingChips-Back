package connectingchips.samchips.user.oauth.client;

import connectingchips.samchips.user.oauth.kakao.dto.KakaoUserResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public interface TestApiClient {

    // AccessToken을 통해 회원 정보 조회
    @GetExchange("http://localhost:8080/test/400")
    void test400();

    @GetExchange("http://localhost:8080/test/500")
    void test500();
}
