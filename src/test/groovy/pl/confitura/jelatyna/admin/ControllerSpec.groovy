package pl.confitura.jelatyna.admin
import groovy.json.JsonBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.MethodArgumentNotValidException
import pl.confitura.jelatyna.AbstractControllerSpec
import pl.confitura.jelatyna.common.TokenGenerator
import pl.confitura.jelatyna.email.EmailService
import spock.lang.Unroll

class ControllerSpec extends AbstractControllerSpec {

    @Autowired
    private Repository repository;

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
          def admins = doGetResponse("/api/admin");
          with(admins[0]) {
              id != null
              roles == ["ADMIN"]
              person.token != null
              person.firstName == "John"
              person.lastName == "Smith"
              person.email == "john@smith.invalid"
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

    def "should get admin by token"() {
        given:
          repository.save(new User(person: new Person(firstName: "John", lastName: "Smith", email: "john@smith.com", token: "TOKEN")))

        when:
          def admin = doGetResponse("/api/admin/create/TOKEN");

        then:
          with(admin) {
              person.firstName == "John"
              person.lastName == "Smith"
          }
    }

    def "should throw exception if admin doesn't exist for token"() {
        given:
          repository.save(new User(person: new Person(firstName: "John", lastName: "Smith", email: "john@smith.com", token: "TOKEN")))

        when:
          def exception = doGet("/api/admin/create/WRONG_TOKEN").resolvedException;

        then:
          exception.class == TokenInvalidException.class
    }

    def "should update admin"() {
        given:
          def user = repository.save(new User(person: new Person(firstName: "John", lastName: "Smith", email: "john@smith.com")))

        when:

          doPut("/api/admin", admin("Bob", 'Smith', 'bob@smith.com', user.getId()))

        then:
          def admins = doGetResponse("/api/admin");
          with(admins[0]) {
              person.firstName == "Bob"
              person.lastName == "Smith"
              person.email == "bob@smith.com"

          }
    }

    private String admin(String firstName, String lastName, String email, Long id = null) {
        def user = [
            person: [
                firstName: firstName,
                lastName : lastName,
                email    : email

            ]
        ]
        if (id != null) {
            user << [id: id]
            user.person << [id: id]
        }
        return new JsonBuilder(user).toString()
    }

    @Override
    def getControllerUnderTest() {
        return new Controller(repository, generator, emailSender)
    }
}
