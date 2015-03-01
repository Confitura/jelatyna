package pl.confitura.jelatyna.email

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import pl.confitura.jelatyna.Application
import pl.confitura.jelatyna.admin.Admin
import spock.lang.Specification

@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = Application.class)
class EmailSenderTest extends Specification {
    @Autowired
    def EmailSender emailSender;

    def "should send email"() {

        expect:
          emailSender.adminCreated(new Admin([email    : "michal.margiel@gmail.com",
                                              firstName: "Michal",
                                              lastName : "Margiel",
                                              token    : "abc"]));

    }
}
