package connectingchips.samchips.global.security.config;

import connectingchips.samchips.global.security.CustomAuthenticationProvider;
import connectingchips.samchips.user.jwt.JwtSecurityConfig;
import connectingchips.samchips.user.jwt.TokenProvider;
import connectingchips.samchips.user.jwt.filter.JwtExceptionFilter;
import connectingchips.samchips.user.jwt.handler.JwtAccessDeniedHandler;
import connectingchips.samchips.global.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final RedisUtils redisUtils;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final CustomAuthenticationProvider customAuthenticationProvider;

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        http.csrf(csrf -> csrf.disable())
            .formLogin(formLogin -> formLogin.disable())
            .httpBasic(httpBasic -> httpBasic.disable());

        http.exceptionHandling((exceptionHanding) ->
                exceptionHanding.accessDeniedHandler(jwtAccessDeniedHandler));

        // 세션을 사용하지 않기 때문에 STATELESS로 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //  ============ 추가한 부분 =============
        http.cors(Customizer.withDefaults());
        // ============= 추가한 부분 =============

        // Spring Security 6.1.0부터는 메서드 체이닝의 사용을 지양하고 람다식을 통해 함수형으로 설정하게 지향
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        new AntPathRequestMatcher("/"),
                        new AntPathRequestMatcher("/h2/**"),
                        new AntPathRequestMatcher("/h2-console/**"),
                        new AntPathRequestMatcher("/swagger-ui/**"),
                        new AntPathRequestMatcher("/error"),
                        // 일단 모든 API 허용되게 설정
                        new AntPathRequestMatcher("/**")

                ).permitAll()
                .anyRequest().authenticated()
        );

        http.headers((headers -> headers
                .frameOptions(frameOptionsConfig ->
                        frameOptionsConfig.disable()
                )
        ));

        http.apply(new JwtSecurityConfig(tokenProvider, redisUtils, jwtExceptionFilter)); // JwtSecurityConfig 적용

        return http.build();
    }
    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(List.of("https://samchips.com","http://localhost:3000","https://samchips-test.netlify.app"));
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Set-Cookie", "*"));
        corsConfiguration.setExposedHeaders(List.of("Authorization", "Set-Cookie"));
        corsConfiguration.setAllowedMethods(List.of("POST", "GET", "PATCH", "DELETE", "OPTIONS","PUT"));
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }
}
