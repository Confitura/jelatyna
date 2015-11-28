package pl.confitura.jelatyna.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import pl.confitura.jelatyna.user.domain.User;

@Component("jelatynaSecurity")
public class JelatynaSecurity {
    public boolean isOwner(String userId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userId.equals(user.getId());
    }
}
