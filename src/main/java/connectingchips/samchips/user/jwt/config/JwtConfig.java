package connectingchips.samchips.user.jwt.config;

import connectingchips.samchips.user.jwt.TokenProvider;
import connectingchips.samchips.user.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean(name = "tokenProvider")
    public TokenProvider tokenProvider(CustomUserDetailsService userDetailsService){
        return new TokenProvider(userDetailsService);
    }
}
