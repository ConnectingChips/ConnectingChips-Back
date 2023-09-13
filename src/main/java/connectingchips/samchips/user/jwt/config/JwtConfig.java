package connectingchips.samchips.user.jwt.config;

import connectingchips.samchips.user.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String accessTokenSecret;
    @Value("${jwt.refresh-token-secret}")
    private String refreshTokenSecret;
    @Value("${jwt.access-token-validity-in-seconds}")
    private Long accessTokenValidityInSeconds;
    @Value("${jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenValidityInSeconds;

    @Bean(name = "tokenProvider")
    public TokenProvider tokenProvider(){
        return new TokenProvider(accessTokenSecret, accessTokenValidityInSeconds);
    }
}
