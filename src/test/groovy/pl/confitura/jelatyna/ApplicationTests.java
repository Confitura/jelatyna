package pl.confitura.jelatyna;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({
    "server.port:0",
    "spring.profiles.active=fake"
})
public class ApplicationTests {

    @Value("${local.server.port}")
    private int port;

    @Test
    public void loginSucceeds() {
        RestTemplate template = new TestRestTemplate("john@example.com", "password");
        ResponseEntity<String> response = template.getForEntity(format("http://localhost:%s/api/user/login", port), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
