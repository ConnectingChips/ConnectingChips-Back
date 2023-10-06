package connectingchips.samchips.security;

import connectingchips.samchips.user.domain.UserAdapter;
import connectingchips.samchips.user.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserAdapter userAdapter = (UserAdapter) userDetailsService.loadUserByUsername(name);

        if(!passwordEncoder.matches(password, userAdapter.getPassword())){
            throw new BadCredentialsException(userAdapter.getUsername() + " Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userAdapter, password, userAdapter.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
