package pl.confitura.jelatyna

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
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
class NewAbstractSpecification extends Specification {
    @Autowired
    RestBuilder rest;

}
