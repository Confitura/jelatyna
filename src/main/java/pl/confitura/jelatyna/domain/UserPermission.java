package pl.confitura.jelatyna.domain;


import org.springframework.security.core.GrantedAuthority;

public enum UserPermission implements GrantedAuthority{
    ADMIN,
    BASIC;

    @Override
    public String getAuthority() {
        return name();
    }

}
