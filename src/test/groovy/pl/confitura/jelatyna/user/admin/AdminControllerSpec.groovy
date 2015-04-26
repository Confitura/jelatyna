package pl.confitura.jelatyna.user.admin

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.MethodArgumentNotValidException
import pl.confitura.jelatyna.AbstractControllerSpec
import pl.confitura.jelatyna.email.EmailService
import pl.confitura.jelatyna.user.TokenGenerator
import pl.confitura.jelatyna.user.UserBuilder
import pl.confitura.jelatyna.user.UserRepository
import pl.confitura.jelatyna.user.domain.Authority
import spock.lang.Unroll

class AdminControllerSpec extends AbstractControllerSpec {

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenGenerator generator;

    private EmailService emailSender = Mock(EmailService);

    @Unroll
    def "should throw exception if admin is invalid"() {
        when:
          def exception = doPost("/api/admin", json).resolvedException;

        then:
          exception.class == MethodArgumentNotValidException.class

        where:
          json << [
              person("", "Smith", "a@a.pl"),
              person("John", "", "a@a.pl"),
              person("John", "Smith", ""),
              person("John", "Smith", "a")
          ]
    }


    def "should create admin"() {
        given:
          def json = person('John', 'Smith', 'john@smith.invalid')

        when:
          doPost("/api/admin", json)

        then:
          def admin = repository.findByEmail('john@smith.invalid');
          with(admin.get()) {
              id != null
              authorities == [Authority.ADMIN]
              person.token != null
              person.firstName == "John"
              person.lastName == "Smith"
              person.email == "john@smith.invalid"
          }
    }

    def "should send email to created admin"() {
        given:
          def json = person('John', 'Smith', 'john@smith.invalid')

        when:
          doPost("/api/admin", json)

        then:
          1 * emailSender.adminCreated({
              it.firstName == "John"
          })
    }

    def "should update a user"() {
        given:
          def user = repository.save(UserBuilder.aUser {})

        when:
          doPut("/api/admin", UserBuilder.aUserAsJson {
              id user.id
              twitter "my twitter"
              code "my code"
              bio "my bio"
              name "Smith John"
              email "smith@john.invalid"
          })

        then:
          with(repository.findOne(user.id).get()) {
              person.email == "smith@john.invalid"
              person.firstName == "Smith"
              person.lastName == "John"
              twitter == "my twitter"
              code == "my code"
              bio == "my bio"
          }
    }

    def "should not update password nor token on updating a user"() {
        given:
          def user = repository.save(UserBuilder.aUser {
              password "password"
              token "token"
          })

        when:
          doPut("/api/admin", UserBuilder.aUserAsJson {
              id user.id
              password ""
              token ""
          })

        then:
          with(repository.findOne(user.id).get()) {
              person.token == "token"
              password == "password"
          }
    }

    def person(fn, ln, em) {
        return UserBuilder.aPersonAsJson {
            firstName fn
            lastName ln
            email em
        }
    }

    @Override
    def getControllerUnderTest() {
        return new AdminController(repository, generator, emailSender)
    }
}
