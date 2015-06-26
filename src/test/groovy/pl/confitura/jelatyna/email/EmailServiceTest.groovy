package pl.confitura.jelatyna.email

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import pl.confitura.jelatyna.Application
import pl.confitura.jelatyna.email.dto.EmailDto
import pl.confitura.jelatyna.email.service.EmailService
import pl.confitura.jelatyna.user.PersonRepository
import pl.confitura.jelatyna.user.domain.Person
import pl.confitura.jelatyna.user.domain.User
import spock.lang.Ignore
import spock.lang.Specification

import javax.transaction.Transactional

@Ignore("This is not a unity test. It is used to check if the integration with mandril is working")
@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = Application.class)
@WebAppConfiguration
@Transactional
@TransactionConfiguration(defaultRollback = true)
@ActiveProfiles("default")
class EmailServiceTest extends Specification {
    @Autowired
    def EmailService service;
    @Autowired
    def PersonRepository repository;

    def "should send email"() {
        expect:
          service.adminCreated(new User(email    : "michal.margiel@gmail.com",
                                         firstName: "Michal",
                                         lastName : "Margiel",
                                         token    : "abc"));

    }

    def "should send email with attachments"() {
        expect:
          repository.save(new Person(firstName: "John", lastName: "Smith", email: "michal.margiel@gmail.com", token: "1"))
          repository.save(new Person(firstName: "Martha", lastName: "Smith", email: "michalmargiel@gmail.com", token: "2"))

          service.send(new EmailDto(audience: "all", includeBarcode: true, template: "ticket"))

    }
}
