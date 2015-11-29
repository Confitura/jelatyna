package pl.confitura.jelatyna;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import pl.confitura.jelatyna.user.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({
        "server.port:0",
        "spring.profiles.active=fake"
})
@Rollback
//@Ignore
public class ApplicationTests {

    @Value("${local.server.port}")
    private int port;
    @Autowired
    private UserRepository repository;

    @After
    public void tearDown() throws Exception {
        repository.deleteAll();
    }

    @Test
    public void should_login_successfully() {
        RestTemplate template = new TestRestTemplate("john@example.com", "password");

        ResponseEntity<String> response = template.getForEntity(format("http://localhost:%s/users/login", port), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
