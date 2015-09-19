package pl.confitura.jelatyna.user.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN, VOLUNTEER, SPEAKER;

    @Override
    public String getAuthority() {
        return name();
    }

}
