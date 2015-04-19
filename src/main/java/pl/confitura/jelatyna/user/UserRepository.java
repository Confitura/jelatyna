package pl.confitura.jelatyna.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import pl.confitura.jelatyna.user.domain.User;

@org.springframework.stereotype.Repository("userRepository")
public interface UserRepository extends org.springframework.data.repository.Repository<User, Long> {

    User save(User user);

    List<User> findAll();

    @Query("FROM User WHERE person.token = ?1")
    Optional<User> findByToken(String token);

}
