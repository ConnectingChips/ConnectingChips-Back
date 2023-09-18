package connectingchips.samchips.user.jwt;

import connectingchips.samchips.user.jwt.filter.JwtAuthenticationFilter;
import connectingchips.samchips.user.jwt.filter.JwtExceptionFilter;
import connectingchips.samchips.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


// SecurityConfigurerAdapter를 상속받아 JwtFilter를 통해 시큐리티 로직에 등록하는 역할 수행
@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final JwtExceptionFilter jwtExceptionFilter;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // security 로직에 JwtFilter 등록
        http.addFilterBefore(
                new JwtAuthenticationFilter(tokenProvider, userRepository),
                UsernamePasswordAuthenticationFilter.class
        ).addFilterBefore(
                jwtExceptionFilter,
                JwtAuthenticationFilter.class
        );
    }
}
