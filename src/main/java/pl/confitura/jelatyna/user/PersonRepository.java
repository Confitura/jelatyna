package pl.confitura.jelatyna.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import pl.confitura.jelatyna.user.domain.Person;

public interface PersonRepository extends Repository<Person, String> {

    Person save(Person person);

    Optional<Person> findOne(String id);

    default List<Person> find(String text) {
        return doFind(text.toLowerCase());
    }

    @Query("FROM Person p " +
            "WHERE registration IS NOT NULL " +
            "AND (lower(p.firstName) like :text% " +
            "OR lower(p.lastName) like :text% " +
            "OR lower(p.email) like :text%)"
    )
    List<Person> doFind(@Param("text") String text);

    List<Person> findAll();

    @Query("FROM Person WHERE registration IS NOT NULL")
    List<Person> findAllRegistered();

    @Query("From Person " +
            "WHERE registration.stampDate IS NOT NULL " +
            "AND drawn = false")
    List<Person> findNotDrawnWithStamps();

    @Modifying
    @Query("UPDATE Registration SET drawn = false")
    void resetDrawing();

    void deleteAll();
}
