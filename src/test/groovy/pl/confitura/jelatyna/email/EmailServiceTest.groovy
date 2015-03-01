package pl.confitura.jelatyna.email
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import pl.confitura.jelatyna.Application
import pl.confitura.jelatyna.admin.Admin
import spock.lang.Specification
//@Ignore("This is not a unity test. It is used to check if the integration is working")
@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = Application.class)
class EmailServiceTest extends Specification {
    @Autowired
    def EmailService emailSender;

    def "should send email"() {

        expect:
          emailSender.adminCreated(new Admin([email    : "michal.margiel@gmail.com",
                                              firstName: "Michal",
                                              lastName : "Margiel",
                                              token    : "abc"]));

    }
}
