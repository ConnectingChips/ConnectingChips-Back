package connectingchips.samchips.user.oauth;

import connectingchips.samchips.user.domain.SocialType;
import connectingchips.samchips.user.oauth.authcode.AuthCodeRequestUrlProviderComposite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;

    public String getAuthCodeRequestUrl(SocialType socialType){
        return authCodeRequestUrlProviderComposite.provideUrl(socialType);
    }
}
