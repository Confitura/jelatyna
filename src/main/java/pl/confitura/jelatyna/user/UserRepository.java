package pl.confitura.jelatyna.user;

import org.springframework.data.jpa.repository.Query;
import pl.confitura.jelatyna.user.domain.User;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository("userRepository")
public interface UserRepository extends org.springframework.data.repository.Repository<User, String> {

    User save(User user);

    List<User> findAll();

    @Query("FROM User WHERE person.token = ?1")
    Optional<User> findByToken(String token);

    @Query("FROM User WHERE person.email = ?1")
    Optional<User> findByEmail(String email);

    Optional<User> findOne(String id);
}
