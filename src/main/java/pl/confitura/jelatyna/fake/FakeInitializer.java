package pl.confitura.jelatyna.fake;

import javax.transaction.Transactional;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import pl.confitura.jelatyna.user.PersonRepository;
import pl.confitura.jelatyna.user.UserRepository;
import pl.confitura.jelatyna.user.domain.Authority;
import pl.confitura.jelatyna.user.domain.Person;
import pl.confitura.jelatyna.user.domain.Registration;
import pl.confitura.jelatyna.user.domain.User;

@Configuration
@Profile({"fake"})
public class FakeInitializer {

    @Bean
    @Transactional
    InitializingBean init(UserRepository repository, PersonRepository personRepository) {
        return () -> {
            repository.save(
                new User()
                    .addAuthority(Authority.VOLUNTEER)
                    .setPassword(new BCryptPasswordEncoder().encode("password"))
                    .setPerson(
                        new Person()
                            .setFirstName("John")
                            .setLastName("Smith")
                            .setEmail("john@example.com")
                            .setToken("asdad")
                    ));
            personRepository
                .save(aPerson("Rob", "Smith", "1", "S", "michal.margiel@gmail.com"));
            personRepository
                .save(aPerson("Martha", "Smith", "2", "XL", "michalmargiel@gmail.com"));
            personRepository
                .save(new Person()
                    .setFirstName("Michal")
                    .setLastName("Margiel")
                    .setEmail("m.ichalmargiel@gmail.com")
                    .setToken("1234"));
        };
    }

    private Person aPerson(String firstName, String lastName, String token, String size, String address) {
        return new Person()
            .setFirstName(firstName)
            .setLastName(lastName)
            .setEmail(address)
            .setToken(token)
            .setRegistration(new Registration().setSize(size));
    }
}
