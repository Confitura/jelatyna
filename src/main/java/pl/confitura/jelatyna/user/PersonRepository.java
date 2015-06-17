package pl.confitura.jelatyna.user;

import org.springframework.data.repository.Repository;
import pl.confitura.jelatyna.user.domain.Person;

import java.util.Optional;

public interface PersonRepository extends Repository<Person, Long> {

    Person save(Person person);

    Optional<Person> findByToken(String token);

}
