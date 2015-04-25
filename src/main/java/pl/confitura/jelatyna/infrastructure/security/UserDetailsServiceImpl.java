package pl.confitura.jelatyna.infrastructure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.confitura.jelatyna.user.UserRepository;
import pl.confitura.jelatyna.user.domain.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String email) throws
            UsernameNotFoundException {
      User user = userRepository
          .findByEmail(email)
          .orElseThrow(() -> new UsernameNotFoundException("Username " + email + " not found"));

      return UserDetails.of(user);
    }
}
