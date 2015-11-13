package pl.confitura.jelatyna

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import pl.confitura.jelatyna.security.SecurityConfiguration
import spock.lang.Specification

import javax.transaction.Transactional

@ContextConfiguration(loader = SpringApplicationContextLoader, classes = [SecurityConfiguration, Application])
@WebAppConfiguration
@Transactional
@Rollback
@ActiveProfiles("test")
class AbstractRestSpecification extends Specification {
    @Autowired
    RestBuilder rest;

    protected RestBuilder path(String path) {
        return rest.path(path)
    }

    protected MockMultipartFile aFile() {
        return new MockMultipartFile("file", "photo.png", null, getClass().getResource("/photo.png").bytes)
    }

}
