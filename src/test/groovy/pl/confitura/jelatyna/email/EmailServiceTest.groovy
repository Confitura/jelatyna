package pl.confitura.jelatyna.email

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import pl.confitura.jelatyna.Application
import pl.confitura.jelatyna.email.dto.EmailDto
import pl.confitura.jelatyna.email.service.EmailService
import pl.confitura.jelatyna.user.PersonRepository
import pl.confitura.jelatyna.user.domain.Person
import pl.confitura.jelatyna.user.domain.Role
import pl.confitura.jelatyna.user.domain.User
import spock.lang.Ignore
import spock.lang.Specification

import javax.transaction.Transactional

@Ignore("This is not a unity test. It is used to check if the integration with mandril is working")
@ContextConfiguration(loader = SpringApplicationContextLoader, classes = Application)
@WebAppConfiguration
@Transactional
@Rollback
@ActiveProfiles("default")
class EmailServiceTest extends Specification {
    @Autowired
    def EmailService service
    @Autowired
    def PersonRepository repository

    def "should send email"() {
        expect:
        service.created(new User(
                token: "abc",
                person:
                        new Person(email: "michal.margiel@gmail.com",
                                firstName: "Michal",
                                lastName: "Margiel")
        ), Role.ADMIN)

    }

    def "should send email with attachments"() {
        expect:
        repository.save(new Person(firstName: "John", lastName: "Smith", email: "michal.margiel@gmail.com"))
        repository.save(new Person(firstName: "Martha", lastName: "Smith", email: "michalmargiel@gmail.com"))

        service.send(new EmailDto(audience: "all", includeBarcode: true, template: "ticket"))

    }
}
