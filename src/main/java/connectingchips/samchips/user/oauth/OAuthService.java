package connectingchips.samchips.user.oauth;

import connectingchips.samchips.user.domain.SocialType;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.oauth.authcode.AuthCodeRequestUrlProviderComposite;
import connectingchips.samchips.user.oauth.client.UserClientComposite;
import connectingchips.samchips.user.oauth.dto.OAuthRequestDto;
import connectingchips.samchips.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final UserClientComposite userClientComposite;
    private final UserRepository userRepository;

    public String getAuthCodeRequestUrl(SocialType socialType){
        return authCodeRequestUrlProviderComposite.provideUrl(socialType);
    }

    @Transactional
    public void socialLogin(OAuthRequestDto.Login loginDto){
        User user = userClientComposite.fetch(SocialType.fromName(loginDto.getSocialType()), loginDto.getAuthCode());

        if(!userRepository.existsByAccountId(user.getAccountId())){
            userRepository.save(user);
        }
    }
}
