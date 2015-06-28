package pl.confitura.jelatyna;

import static java.lang.String.format;
import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import pl.confitura.jelatyna.user.PersonRepository;
import pl.confitura.jelatyna.user.UserRepository;
import pl.confitura.jelatyna.user.domain.Authority;
import pl.confitura.jelatyna.user.domain.Person;
import pl.confitura.jelatyna.user.domain.Registration;
import pl.confitura.jelatyna.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({
        "server.port:0",
        "spring.profiles.active=default"
})
@TransactionConfiguration(defaultRollback = false)
public class ApplicationTests {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private PersonRepository repository;
    @Autowired
    private UserRepository userRepository;


    @Test
    @Ignore
    public void loginSucceeds() {
        RestTemplate template = new TestRestTemplate("john@example.com", "password");
        ResponseEntity<String> response = template.getForEntity(format("http://localhost:%s/api/user/login", port), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Ignore
    public void import_participants() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Path path = Paths.get("/Users/margielm/jelatyna/participants.csv");
        Files.lines(path)
                .skip(1)
                .map(line -> line.split(";"))
                .map(row -> new Person()
                        .setFirstName(get(row[0]))
                        .setLastName(get(row[1]))
                        .setEmail(get(row[2]))
                        .setToken(get(row[3]))
                        .setRegistration(
                                new Registration()
                                        .setCity(get(row[4]))
                                        .setExperience(get(row[5]))
                                        .setPosition(get(row[6]))
                                        .setRegistrationDate(LocalDateTime.parse(get(row[7]), formatter))
                                        .setSex(get(row[8]))
                                        .setSize(get(row[9]))
                        ))
                .map(person -> new User()
                        .setPerson(person)
                        .addAuthority(Authority.VOLUNTEER))
                .forEach(userRepository::save);

    }

    private String get(String value) {
        return value.replaceAll("\"", "");
    }
}
