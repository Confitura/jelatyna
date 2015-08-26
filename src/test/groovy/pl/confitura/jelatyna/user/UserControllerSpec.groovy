package pl.confitura.jelatyna.user

import groovy.json.JsonBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.multipart.MultipartFile
import pl.confitura.jelatyna.AbstractControllerSpec
import pl.confitura.jelatyna.email.service.EmailService
import pl.confitura.jelatyna.user.dto.NewUser
import pl.confitura.jelatyna.user.dto.UserDto
import spock.lang.Unroll

import static pl.confitura.jelatyna.user.domain.Role.ADMIN

class UserControllerSpec extends AbstractControllerSpec {

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenGenerator generator;

    private EmailService emailSender = Mock(EmailService);

    @Unroll
    def "should throw exception if admin is invalid"() {
        when:
        def exception = doPost("/users", json).resolvedException;

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
        doPost("/users", new JsonBuilder(newUser).toString())

        then:
        def admin = repository.findByEmail('john@smith.invalid');
        with(admin.get()) {
            id != null
            roles == [ADMIN]
            person.firstName == "John"
            person.lastName == "Smith"
            person.email == "john@smith.invalid"
        }
    }

    def "should send email to created admin"() {
        given:
        def newUser = new NewUser(firstName: 'John', lastName: 'Smith', email: 'john@smith.invalid', role: ADMIN)

        when:
        doPost("/users", new JsonBuilder(newUser).toString())

        then:
        1 * emailSender.adminCreated({
            it.firstName == "John"
        })
    }

    def "should update a user"() {
        given:
        def user = repository.save(UserBuilder.aUser {})

        when:
        doPatch("/users", new JsonBuilder(new UserDto(
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
        MultipartFile file = new MockMultipartFile("file", "photo.png", null, getClass().getResource("/photo.png").getBytes())

        when:
        mockMvc.perform(
                MockMvcRequestBuilders
                        .fileUpload("/users/$user.id/photo").file(file))
                .andReturn();
        then:
        file.getBytes() == doGet("/users/$user.id/photo").getResponse().getContentAsByteArray()
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
