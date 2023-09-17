package connectingchips.samchips.user.oauth.kakao.config;

import connectingchips.samchips.user.oauth.kakao.client.KakaoApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpInterfaceConfig {

    @Bean
    public KakaoApiClient kakaoApiClient(){
        return createHttpInterface(KakaoApiClient.class);
    }

    // 해당 인터페이스에 대한 프록시 구현체를 만들어줘야 하는데, 프록시를 생성하는데 WebClient를 필요로 한다.
    private <T> T createHttpInterface(Class<T> _class){
        WebClient webClient = WebClient.create();
        HttpServiceProxyFactory build = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(webClient)).build();
        return build.createClient(_class);
    }
}
