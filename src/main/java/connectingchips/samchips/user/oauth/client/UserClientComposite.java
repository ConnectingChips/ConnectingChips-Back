package connectingchips.samchips.user.oauth.client;

import connectingchips.samchips.user.domain.SocialType;
import connectingchips.samchips.user.domain.User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Component
public class UserClientComposite {

    private final Map<SocialType, UserClient> maps;

    public UserClientComposite(Set<UserClient> clients) {
        maps = clients.stream()
                .collect(toMap(
                        UserClient::supportServer,
                        identity()
                ));
    }

    public User fetch(SocialType socialType, String authCode){
        return getClient(socialType).fetch(authCode);
    }

    private UserClient getClient(SocialType socialType){
        return Optional.ofNullable(maps.get(socialType))
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 소셜 로그인 타입입니다."));
    }
}
