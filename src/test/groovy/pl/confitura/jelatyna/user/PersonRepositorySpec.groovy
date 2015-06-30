package pl.confitura.jelatyna.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.transaction.TransactionConfiguration
import pl.confitura.jelatyna.Application
import pl.confitura.jelatyna.user.domain.Person
import pl.confitura.jelatyna.user.domain.Registration
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import javax.transaction.Transactional

@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = Application.class)
@Transactional
@TransactionConfiguration(defaultRollback = true)
@ActiveProfiles("test")
class PersonRepositorySpec extends Specification {

    @Autowired
    def PersonRepository repository;
    @Shared
    def john;
    @Shared
    def rob;

    def setup() {
        john = repository.save(new Person(firstName: 'John', lastName: 'Smith', token: '3', email: 'j@s.com',
            registration: new Registration(size: 'S')))
        rob = repository.save(new Person(firstName: 'Rob', lastName: 'Martin', token: '4', email: 'r@m.com',
            registration: new Registration(size: 'M')))
    }

    def "should find a person by token"() {
        when:
          def person = repository.findByToken('3');

        then:
          with(person.get()) {
              firstName == 'John'
              lastName == 'Smith'
          }
    }

    @Unroll
    def "should find a person by first name, last name or email"() {
        when:
          def person = repository.find(text);

        then:
          with(person.get(0)) {
              firstName == firstName
          }

        where:
          text      || firstName
          "rob"     || "Rob"
          "jo"      || "John"
          "SMITH"   || "John"
          "mar"     || "Rob"
          "r@m.com" || "Rob"
          "j@"      || "John"
    }
}
