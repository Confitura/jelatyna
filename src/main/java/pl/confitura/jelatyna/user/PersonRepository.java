package pl.confitura.jelatyna.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import pl.confitura.jelatyna.user.domain.Person;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends Repository<Person, Long> {

    Person save(Person person);

    Optional<Person> findByToken(String token);


    @Query("FROM Person p " +
        "WHERE lower(p.firstName) like :text% " +
        "OR lower(p.lastName) like :text% " +
        "OR lower(p.email) like :text% "
    )
    List<Person> doFind(@Param("text") String text);

    default List<Person> find(String text) {
        return doFind(text.toLowerCase());
    }

    List<Person> findAll();

    @Query("FROM Person WHERE registration IS NOT NULL")
    List<Person> findAllRegistered();
}
