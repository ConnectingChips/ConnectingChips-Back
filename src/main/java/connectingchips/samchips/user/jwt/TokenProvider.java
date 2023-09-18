package connectingchips.samchips.user.jwt;

import connectingchips.samchips.exception.RestApiException;
import connectingchips.samchips.user.service.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static connectingchips.samchips.exception.AuthErrorCode.*;
import static connectingchips.samchips.user.jwt.filter.JwtAuthenticationFilter.AUTHORIZATION_HEADER;

@Slf4j
@RequiredArgsConstructor
public class TokenProvider {

    private final CustomUserDetailsService userDetailsService;

    protected static final String AUTHORITIES_KEY = "auth";

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access-token-expiration-millis}")
    private long accessTokenExpirationMillis;
    @Value("${jwt.refresh-token-expiration-millis}")
    private long refreshTokenExpirationMillis;
    private Key key;

    // Bean 등록후 Key SecretKey HS256 decode
    @PostConstruct
    public void init(){
        accessTokenExpirationMillis *= 1000;
        refreshTokenExpirationMillis *= 1000;

        // 시크릿 값을 decode해서 키 변수에 할당
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 엑세스 토큰 생성
    public String createAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + accessTokenExpirationMillis);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                // signature 에 들어갈 secret값 사용할 암호화 알고리즘
                .signWith(key, SignatureAlgorithm.HS256)
                // set Expire Time 해당 옵션 안넣으면 expire안함
                .setExpiration(validity)
                .compact();
    }

    // 리프레쉬 토큰 생성
    public String createRefreshToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + refreshTokenExpirationMillis);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                // signature 에 들어갈 secret값 사용할 암호화 알고리즘
                .signWith(key, SignatureAlgorithm.HS256)
                // set Expire Time 해당 옵션 안넣으면 expire안함
                .setExpiration(validity)
                .compact();
    }

    // 토큰으로 클레임을 만들고 이를 이용해 유저 객체를 만들어서 최종적으로 authentication 객체를 리턴
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }

    // 헤더에서 토큰 정보를 꺼내온다.
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("유효하지 않은 JWT 서명입니다.");
            throw new RestApiException(INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            throw new RestApiException(EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.info("지원하지 않는 JWT 토큰입니다.");
            throw new RestApiException(UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("잘못된 JWT 토큰입니다.");
            throw new RestApiException(ILLEGAL_ARGUMENT_TOKEN);
        }
    }
}
