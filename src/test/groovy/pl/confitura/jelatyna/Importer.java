package pl.confitura.jelatyna;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import com.google.common.base.Charsets;

import pl.confitura.jelatyna.user.PersonRepository;
import pl.confitura.jelatyna.user.domain.Person;
import pl.confitura.jelatyna.user.domain.Registration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({
        "server.port=0",
        "spring.profiles.active=default",
        "spring.datasource.url=jdbc:mysql://localhost:33061/confitura-next?useUnicode=yes&characterEncoding=UTF-8"
})
@TransactionConfiguration(defaultRollback = false)
public class Importer {

    @Autowired
    private PersonRepository repository;

    @Test
    @Ignore
    public void import_participants() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Path path = Paths.get("/Users/margielm/jelatyna/participants_4.csv");
        Files.lines(path, Charsets.UTF_8)
                .skip(1)
                .map(line -> line.split(";"))
                .filter(row -> !repository.findByToken(get(row[3])).isPresent())
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

                .forEach(repository::save);

    }

    private String get(String value) {
        return value.replaceAll("\"", "");
    }
}
