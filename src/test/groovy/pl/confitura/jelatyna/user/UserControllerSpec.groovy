package pl.confitura.jelatyna.user
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.multipart.MultipartFile
import pl.confitura.jelatyna.AbstractRestSpecification
import pl.confitura.jelatyna.email.service.EmailService
import spock.lang.Unroll

import static pl.confitura.jelatyna.user.domain.Role.*

class UserControllerSpec extends AbstractRestSpecification {

    @Autowired
    private UserRepository repository

    @Autowired
    private TokenGenerator generator

    private EmailService emailSender = Mock(EmailService)

    void setup() {
        path("/users").forController(new UserController(repository, generator, emailSender))
    }

    @Unroll
    def "should throw exception if admin is invalid"() {
        when:
        def exception = rest.post(json).getException()

        then:
        exception.class == MethodArgumentNotValidException

        where:
        json << [
                newUser("", "Smith", "a@a.pl"),
                newUser("John", "", "a@a.pl"),
                newUser("John", "Smith", ""),
                newUser("John", "Smith", "a")
        ]
    }

    @Unroll("should create user with #role role")
    def "should create user"() {
        given:
        def user = [firstName: 'John', lastName: 'Smith', email: 'john@smith.invalid', role: role]

        when:
        def id = rest.post(user).getId()

        then:
        with(rest.get(id)) {
            id == id
            roles == [role.name()]
            firstName == "John"
            lastName == "Smith"
            email == "john@smith.invalid"
        }
        where:
        role << [ADMIN, SPEAKER, VOLUNTEER]
    }

    def "should send email to created user"() {
        given:
        def user = [firstName: 'John', lastName: 'Smith', email: 'john@smith.invalid', role: ADMIN]

        when:
        rest.post(user)

        then:
        interaction {
            1 * emailSender.created(
                    { it.person.email == user.email },
                    user.role)
        }
    }

    def "should update a user"() {
        given:
        def user = repository.save(UserBuilder.aUser {})

        when:
        rest.patch(
                [id       : user.id,
                 twitter  : "my twitter",
                 code     : "my code",
                 bio      : "my bio",
                 firstName: "Smith",
                 lastName : "John",
                 email    : "smith@john.invalid"]
        )

        then:
        with(rest.get(user.id)) {
            email == "smith@john.invalid"
            firstName == "Smith"
            lastName == "John"
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
        rest.patch([
                id      : user.id,
                password: "",
                token   : ""
        ])

        then:
        with(repository.findOne(user.id).get()) {
            token == "token"
            password == "password"
        }
    }

    def "should upload a picture"() {
        given:
        def user = repository.save(UserBuilder.aUser {})
        MultipartFile file = aFile()

        when:
        path("/users/$user.id/photo").upload(file)

        then:
        file.bytes == rest.getAsResponse("/users/$user.id/photo").contentAsByteArray
    }

    Map newUser(firstName, lastName, email) {
        return [
                firstName: firstName,
                lastName : lastName,
                email    : email
        ]
    }

}
