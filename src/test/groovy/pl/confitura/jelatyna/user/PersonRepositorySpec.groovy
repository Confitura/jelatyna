package pl.confitura.jelatyna.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.transaction.TransactionConfiguration
import pl.confitura.jelatyna.Application
import pl.confitura.jelatyna.user.domain.Person
import pl.confitura.jelatyna.user.domain.Registration
import spock.lang.Specification

import javax.transaction.Transactional

@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = Application.class)
@Transactional
@TransactionConfiguration(defaultRollback = true)
@ActiveProfiles("fake")
class PersonRepositorySpec extends Specification {

    @Autowired
    def PersonRepository repository;

    def "should create person with registration"() {
        given:
          repository.save(new Person(firstName: 'John', lastName: 'Smith', token: '1', email: 'j@s.com',
              registration: new Registration(size: 'S')))

        when:
          def person = repository.findByToken('1');

        then:
          with(person.get()) {
              firstName == 'John'
              lastName == 'Smith'
          }
    }
}
