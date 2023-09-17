package connectingchips.samchips.user.oauth;

import connectingchips.samchips.user.domain.SocialType;
import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.dto.AuthResponseDto;
import connectingchips.samchips.user.jwt.TokenProvider;
import connectingchips.samchips.user.oauth.authcode.AuthCodeRequestUrlProviderComposite;
import connectingchips.samchips.user.oauth.client.UserClientComposite;
import connectingchips.samchips.user.oauth.dto.OAuthRequestDto;
import connectingchips.samchips.user.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;

    public String getAuthCodeRequestUrl(SocialType socialType){
        return authCodeRequestUrlProviderComposite.provideUrl(socialType);
    }

    @Transactional
    public AuthResponseDto.Token socialLogin(OAuthRequestDto.Login loginDto){
        User user = userClientComposite.fetch(SocialType.fromName(loginDto.getSocialType()), loginDto.getAuthCode());

        User findUser = saveOrFind(user);

        // 입력한 accountId와 password로 UsernamePasswordAuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(findUser.getAccountId(), findUser.getPassword());

        // authenticate 메소드가 실행이 될 때 CustomUserDetailsService의 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // authentication 객체로 token 생성
        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        findUser.editRefreshToken(refreshToken);

        return new AuthResponseDto.Token(accessToken, refreshToken);
    }

    private User saveOrFind(User user){
        if(!userRepository.existsByAccountId(user.getAccountId())){
            User createUser = User.builder()
                    .accountId(user.getAccountId())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .nickname(user.getNickname())
                    .email(user.getEmail())
                    .gender(user.getGender())
                    .ageRange(user.getAgeRange())
                    .socialType(user.getSocialType())
                    .build();

            return userRepository.save(createUser);
        }else{
            return userRepository.findByAccountId(user.getAccountId()).get();
        }
    }
}
