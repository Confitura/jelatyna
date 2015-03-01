package pl.confitura.jelatyna.admin

import groovy.json.JsonBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.MethodArgumentNotValidException
import pl.confitura.jelatyna.AbstractRestSpec
import pl.confitura.jelatyna.common.TokenGenerator
import pl.confitura.jelatyna.email.EmailSender
import spock.lang.Unroll

class ControllerSpec extends AbstractRestSpec {

    @Autowired
    private Repository repository;
    @Autowired
    private TokenGenerator generator;
    private EmailSender emailSender = Mock(EmailSender);

    @Unroll
    def "should throw exception if admin is invalid"() {
        when:
          def exception = doPost("/api/admin", json).resolvedException;

        then:
          exception.class == MethodArgumentNotValidException.class

        where:
          json << [
              admin("", "Smith", "a@a.pl"),
              admin("John", "", "a@a.pl"),
              admin("John", "Smith", ""),
              admin("John", "Smith", "a")
          ]
    }


    def "should create admin"() {
        given:
          def json = admin('John', 'Smith', 'john@smith.invalid')

        when:
          doPost("/api/admin", json)

        then:
          def admins = doGet("/api/admin");
          with(admins[0]) {
              id != null
              token != null
              firstName == "John"
              lastName == "Smith"
              email == "john@smith.invalid"
          }
    }

    def "should send email to created admin"() {
        given:
          def json = admin('John', 'Smith', 'john@smith.com')

        when:
          doPost("/api/admin", json)

        then:
          1 * emailSender.adminCreated({
              it.firstName == "John"
          })
    }

    def "should update admin"() {
        given:
          def user = repository.save(new Admin(firstName: "John", lastName: "Smith", email: "john@smith.com"))

        when:

          doPut("/api/admin", admin("Bob", 'Smith', 'bob@smith.com', user.getId()))

        then:
          def admins = doGet("/api/admin");
          with(admins[0]) {
              firstName: "Bob"
              lastName: "Smith"
              email: "bob@smith.com"

          }
    }

    private String admin(String firstName, String lastName, String email, Long id = null) {
        def user = [
            firstName: firstName,
            lastName : lastName,
            email    : email

        ]
        if (id != null) {
            user << [id: id]
        }
        def builder = new JsonBuilder(user)
        return builder.toString()
    }

    @Override
    def getControllerUnderTest() {
        return new Controller(repository, generator, emailSender)
    }
}
