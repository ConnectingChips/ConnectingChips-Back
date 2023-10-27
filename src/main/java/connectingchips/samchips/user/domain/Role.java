package connectingchips.samchips.user.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_USER,ROLE_ADMIN");

    private final String role;

    public String getRole() {
        return role;
    }
}
