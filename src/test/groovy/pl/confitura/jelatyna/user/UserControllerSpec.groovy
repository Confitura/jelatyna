package pl.confitura.jelatyna.user

import groovy.json.JsonBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.multipart.MultipartFile
import pl.confitura.jelatyna.AbstractControllerSpec
import pl.confitura.jelatyna.email.service.EmailService
import pl.confitura.jelatyna.user.dto.NewUser
import pl.confitura.jelatyna.user.dto.UserDto
import spock.lang.Unroll

import static pl.confitura.jelatyna.user.domain.Role.*

class UserControllerSpec extends AbstractControllerSpec {

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenGenerator generator;

    @Autowired
    private UserController controller;

    private EmailService emailSender = Mock(EmailService);

    @Unroll
    def "should throw exception if admin is invalid"() {
        when:
        def exception = doPost("/users", json).resolvedException;

        then:
        exception.class == MethodArgumentNotValidException

        where:
        json << [
                person("", "Smith", "a@a.pl"),
                person("John", "", "a@a.pl"),
                person("John", "Smith", ""),
                person("John", "Smith", "a")
        ]
    }

    @Unroll("should create user with #role role")
    def "should create user"() {
        given:
        def newUser = new NewUser(firstName: 'John', lastName: 'Smith', email: 'john@smith.invalid', role: role)

        when:
        def result = doPost("/users", new JsonBuilder(newUser).toString());

        then:
        def id = getId(result)
        with(get("/users/$id")) {
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
        def newUser = new NewUser(firstName: 'John', lastName: 'Smith', email: 'john@smith.invalid', role: ADMIN)

        when:
        doPost("/users", new JsonBuilder(newUser).toString())

        then:
        interaction {
            1 * emailSender.created(
                    { it.person.email == newUser.email },
                    newUser.role)
        }
    }

    def "should update a user"() {
        given:
        def user = repository.save(UserBuilder.aUser {})

        when:
        def result = doPatch("/users", new JsonBuilder(new UserDto(
                id: user.id,
                twitter: "my twitter",
                code: "my code",
                bio: "my bio",
                firstName: "Smith",
                lastName: "John",
                email: "smith@john.invalid"
        )).toString())

        then:
        with(get("/users/$user.id")) {
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
        doPut("/users", UserBuilder.aUserAsJson {
            id user.id
            password ""
            token ""
        })

        then:
        with(repository.findOne(user.id).get()) {
            token == "token"
            password == "password"
        }
    }

    def "should upload a picture"() {
        given:
        def user = repository.save(UserBuilder.aUser {})
        MultipartFile file = new MockMultipartFile("file", "photo.png", null, getClass().getResource("/photo.png").bytes)

        when:
        uploadFile("/users/$user.id/photo", file)

        then:
        file.bytes == doGet("/users/$user.id/photo").response.contentAsByteArray
    }

    String person(fn, ln, em) {
        return UserBuilder.aPersonAsJson {
            firstName fn
            lastName ln
            email em
        }
    }

    @Override
    UserController getControllerUnderTest() {
        return new UserController(repository, generator, emailSender)
    }
}
