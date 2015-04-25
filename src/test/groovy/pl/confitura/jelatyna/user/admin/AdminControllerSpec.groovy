package pl.confitura.jelatyna.user.admin
import groovy.json.JsonBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.MethodArgumentNotValidException
import pl.confitura.jelatyna.AbstractControllerSpec
import pl.confitura.jelatyna.user.TokenGenerator
import pl.confitura.jelatyna.email.EmailService
import pl.confitura.jelatyna.user.UserRepository
import pl.confitura.jelatyna.user.domain.Role
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
            roles == [Role.ADMIN]
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


    private String person(String firstName, String lastName, String email, Long id = null) {
        def person = [
                firstName: firstName,
                lastName : lastName,
                email    : email
        ]
        if (id != null) {
            person << [id: id]
        }
        return new JsonBuilder(person).toString()
    }

    @Override
    def getControllerUnderTest() {
        return new AdminController(repository, generator, emailSender)
    }
}
