package pl.confitura.jelatyna;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.confitura.jelatyna.user.PersonRepository;
import pl.confitura.jelatyna.user.UserRepository;
import pl.confitura.jelatyna.user.domain.Authority;
import pl.confitura.jelatyna.user.domain.Person;
import pl.confitura.jelatyna.user.domain.Registration;
import pl.confitura.jelatyna.user.domain.User;

import javax.transaction.Transactional;

import static java.lang.String.format;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @Transactional
    InitializingBean init(UserRepository repository, PersonRepository personRepository) {
        return () -> {
            repository.save(
                new User()
                    .addAuthority(Authority.ADMIN)
                    .setPassword(new BCryptPasswordEncoder().encode("password"))
                    .setPerson(
                        new Person()
                            .setFirstName("John")
                            .setLastName("Smith")
                            .setEmail("john@smith.com")

                    ));
            personRepository
                .save(aPerson("Rob", "Smith", "1", "S"));
            personRepository
                .save(aPerson("Martha", "Smith", "2", "XL"));
        };
    }

    private Person aPerson(String firstName, String lastName, String token, String size) {
        return new Person()
            .setFirstName(firstName)
            .setLastName(lastName)
            .setEmail(format("%s@%s.com", firstName, lastName))
            .setToken(token)
            .setRegistration(new Registration().setSize(size));
    }
}
