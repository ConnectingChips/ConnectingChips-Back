package connectingchips.samchips.user.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

public class UserAdapter extends org.springframework.security.core.userdetails.User {

    private User user;

    public UserAdapter(User user){
        super(user.getAccountId(), user.getPassword(), authorities(user.getRoles()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    private static List<GrantedAuthority> authorities(Role roles) {
        return Collections.singletonList(new SimpleGrantedAuthority(roles.getRole()));
    }
}
