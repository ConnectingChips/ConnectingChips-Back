package connectingchips.samchips.user.service;

import connectingchips.samchips.user.domain.User;
import connectingchips.samchips.user.domain.UserAdapter;
import connectingchips.samchips.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {

        User findUser = userService.getByAccountId(accountId);

        return new UserAdapter(findUser);
    }
}