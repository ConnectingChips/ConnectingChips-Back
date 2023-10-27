package connectingchips.samchips.user.oauth.client;

import connectingchips.samchips.user.domain.SocialType;
import connectingchips.samchips.user.domain.User;

public interface UserClient {

    SocialType supportServer();

    // 인자로 Auth Code를 받는다.
    User fetch(String authCode);
}
