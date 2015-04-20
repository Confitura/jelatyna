package pl.confitura.jelatyna.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.confitura.jelatyna.repository.ParticipantRepository;
import pl.confitura.jelatyna.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, ParticipantRepository participantRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws
            UsernameNotFoundException {
        return userRepository
                .findByParticipantEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + email + " not found"));
    }
}
