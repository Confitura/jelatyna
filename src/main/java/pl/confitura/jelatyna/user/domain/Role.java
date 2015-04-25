package pl.confitura.jelatyna.user.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
}
