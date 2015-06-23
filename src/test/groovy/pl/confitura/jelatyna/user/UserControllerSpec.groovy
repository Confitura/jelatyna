package pl.confitura.jelatyna.user

import groovy.json.JsonBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.MethodArgumentNotValidException
import pl.confitura.jelatyna.AbstractControllerSpec
import pl.confitura.jelatyna.email.service.EmailService
import pl.confitura.jelatyna.user.dto.NewUser
import pl.confitura.jelatyna.user.dto.UserDto
import spock.lang.Unroll

import static pl.confitura.jelatyna.user.domain.Authority.ADMIN

class UserControllerSpec extends AbstractControllerSpec {

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenGenerator generator;

    private EmailService emailSender = Mock(EmailService);

    @Unroll
    def "should throw exception if admin is invalid"() {
        when:
          def exception = doPost("/api/user", json).resolvedException;

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
          def newUser = new NewUser(firstName: 'John', lastName: 'Smith', email: 'john@smith.invalid', role: ADMIN)

        when:
          doPost("/api/user", new JsonBuilder(newUser).toString())

        then:
          def admin = repository.findByEmail('john@smith.invalid');
          with(admin.get()) {
              id != null
              authorities == [ADMIN]
              person.token != null
              person.firstName == "John"
              person.lastName == "Smith"
              person.email == "john@smith.invalid"
          }
    }

    def "should send email to created admin"() {
        given:
          def newUser = new NewUser(firstName: 'John', lastName: 'Smith', email: 'john@smith.invalid', role: ADMIN)

        when:
          doPost("/api/user", new JsonBuilder(newUser).toString())

        then:
          1 * emailSender.adminCreated({
              it.firstName == "John"
          })
    }

    def "should update a user"() {
        given:
          def user = repository.save(UserBuilder.aUser {})

        when:
          doPut("/api/user", new JsonBuilder(new UserDto(
              id: user.id,
              twitter: "my twitter",
              code: "my code",
              bio: "my bio",
              firstName: "Smith",
              lastName: "John",
              email: "smith@john.invalid"
          )).toString())

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
          doPut("/api/user", UserBuilder.aUserAsJson {
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
        return new UserController(repository, generator, emailSender)
    }
}
