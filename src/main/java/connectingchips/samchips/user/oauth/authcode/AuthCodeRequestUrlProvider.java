package connectingchips.samchips.user.oauth.authcode;

import connectingchips.samchips.user.domain.SocialType;

/**
 * AuthCode를 발급할 URL을 제공하는 기능을 제공
 */
public interface AuthCodeRequestUrlProvider {

    SocialType supportServer();

    String provideUrl();
}
