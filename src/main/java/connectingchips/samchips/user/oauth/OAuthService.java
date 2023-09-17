package connectingchips.samchips.user.oauth;

import connectingchips.samchips.user.domain.SocialType;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.dto.AuthResponseDto;
import connectingchips.samchips.user.jwt.TokenProvider;
import connectingchips.samchips.user.oauth.authcode.AuthCodeRequestUrlProviderComposite;
import connectingchips.samchips.user.oauth.client.UserClientComposite;
import connectingchips.samchips.user.oauth.dto.OAuthRequestDto;
import connectingchips.samchips.user.repository.UserRepository;
import connectingchips.samchips.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final UserClientComposite userClientComposite;
    private final AuthService authService;

    public String getAuthCodeRequestUrl(SocialType socialType){
        return authCodeRequestUrlProviderComposite.provideUrl(socialType);
    }

    @Transactional
    public AuthResponseDto.Token socialLogin(OAuthRequestDto.Login loginDto){
        User user = userClientComposite.fetch(SocialType.fromName(loginDto.getSocialType()), loginDto.getAuthCode());

        AuthResponseDto.Token token = authService.authenticateSocial(user);

        return token;
    }
}
