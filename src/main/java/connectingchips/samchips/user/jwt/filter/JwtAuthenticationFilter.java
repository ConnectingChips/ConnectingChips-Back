package connectingchips.samchips.user.jwt.filter;

import connectingchips.samchips.exception.RestApiException;
import connectingchips.samchips.user.jwt.TokenProvider;
import connectingchips.samchips.user.repository.UserRepository;
import connectingchips.samchips.utils.RedisUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static connectingchips.samchips.exception.AuthErrorCode.EXPIRED_TOKEN;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final TokenProvider tokenProvider;
    private final RedisUtils redisUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = tokenProvider.resolveToken(request);
        String requestURI = request.getRequestURI();

        // 유효성 검증
        if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)) {
            // 토큰에서 유저네임, 권한을 뽑아 스프링 시큐리티 유저를 만들어 Authentication 반환
            Authentication authentication = tokenProvider.getAuthentication(accessToken);

            // Redis에 BlackList로 추가된 AccessToken이라면 만료 예외 발생
            if(redisUtils.isExists(accessToken)){
                throw new RestApiException(EXPIRED_TOKEN);
            }

            // 해당 스프링 시큐리티 유저를 시큐리티 컨텍스트에 저장, 즉 디비를 거치지 않음
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Security Context에 {} 인증 정보를 저장했습니다. uri: {}", authentication.getName(), requestURI);
        } else {
            log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        filterChain.doFilter(request, response);
    }
}
