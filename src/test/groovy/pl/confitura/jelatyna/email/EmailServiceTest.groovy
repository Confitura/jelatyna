package pl.confitura.jelatyna.email

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import pl.confitura.jelatyna.Application
import pl.confitura.jelatyna.email.service.EmailService
import pl.confitura.jelatyna.user.domain.User
import spock.lang.Ignore
import spock.lang.Specification

@Ignore("This is not a unity test. It is used to check if the integration with mandril is working")
@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = Application.class)
class EmailServiceTest extends Specification {
    @Autowired
    def EmailService emailSender;

    def "should send email"() {

        expect:
          emailSender.adminCreated(new User([email    : "michal.margiel@gmail.com",
                                             firstName: "Michal",
                                             lastName : "Margiel",
                                             token    : "abc"]));

    }
}
