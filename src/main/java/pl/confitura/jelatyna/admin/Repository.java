package pl.confitura.jelatyna.admin;

import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository("userRepository")
public interface Repository extends org.springframework.data.repository.Repository<User, Long> {

    User save(User user);

    List<User> findAll();

    @Query("FROM User WHERE person.token = ?1")
    Optional<User> findByToken(String token);

}
