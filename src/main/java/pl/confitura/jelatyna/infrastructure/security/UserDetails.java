package pl.confitura.jelatyna.infrastructure.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import pl.confitura.jelatyna.user.domain.User;

import java.util.Collection;

@Data
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    public static UserDetails of(User user) {
        return new UserDetails(user);
    }

    private final User user;

    private UserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getPerson().getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
