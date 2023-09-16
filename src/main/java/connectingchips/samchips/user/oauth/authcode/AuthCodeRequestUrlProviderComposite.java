package connectingchips.samchips.user.oauth.authcode;

import connectingchips.samchips.user.domain.SocialType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * SocialType에 따라, AuthCodeRequestUrlProvider를 사용하여 URL을 생성할 수 있도록 하는 Composite
 */
@Component
public class AuthCodeRequestUrlProviderComposite {

    private final Map<SocialType, AuthCodeRequestUrlProvider> maps;

    public AuthCodeRequestUrlProviderComposite(Set<AuthCodeRequestUrlProvider> providers) {
        maps = providers.stream()
                .collect(toMap(
                        AuthCodeRequestUrlProvider::supportServer,
                        identity()
                ));
    }

    public String provideUrl(SocialType socialType){
        return getProvider(socialType).provideUrl();
    }

    public AuthCodeRequestUrlProvider getProvider(SocialType socialType){
        return Optional.ofNullable(maps.get(socialType))
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 소셜 로그인 타입입니다."));
    }
}
