package connectingchips.samchips.user.service;

import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.dto.AuthResponseDto;
import connectingchips.samchips.user.dto.UserRequestDto;
import connectingchips.samchips.user.jwt.TokenProvider;
import connectingchips.samchips.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;

    @Transactional
    public AuthResponseDto.Token login(UserRequestDto.Login loginDto){
        // 입력한 accountId와 password로 UsernamePasswordAuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getAccountId(), loginDto.getPassword());

        // authenticate 메소드가 실행이 될 때 CustomUserDetailsService의 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        User user = userRepository.findByAccountId(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 accountId입니다."));

        // authentication 객체로 token 생성
        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        user.editRefreshToken(refreshToken);

        return new AuthResponseDto.Token(accessToken, refreshToken);
    }

    @Transactional
    public void logout(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 userId입니다."));

        // 로그아웃 시, DB의 token 데이터 초기화
        user.editRefreshToken(null);
    }

    @Transactional(readOnly = true)
    public AuthResponseDto.Token reissueAccessToken(String refreshToken){
//        String refreshToken = reissueDto.getRefreshToken();

        // 리프레시 토큰 검증
        if(!tokenProvider.validateToken(refreshToken)){
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        // 리프레시 토큰 값을 이용해 사용자를 꺼낸다.
        Authentication authentication = tokenProvider.getAuthentication(refreshToken);
        User user = userRepository.findByAccountId(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 accountId입니다."));

        if(!user.getRefreshToken().equals(refreshToken)){
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        // 리프레시 토큰에 담긴 값을 그대로 액세스 토큰 생성에 활용한다.
        String accessToken = tokenProvider.createAccessToken(authentication);

        return new AuthResponseDto.Token(accessToken, refreshToken);
    }
}
