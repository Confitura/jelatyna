package pl.confitura.jelatyna.user.password

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import pl.confitura.jelatyna.AbstractControllerSpec
import pl.confitura.jelatyna.email.service.EmailService
import pl.confitura.jelatyna.user.TokenGenerator
import pl.confitura.jelatyna.user.TokenInvalidException
import pl.confitura.jelatyna.user.UserRepository
import pl.confitura.jelatyna.user.domain.User

import static pl.confitura.jelatyna.user.UserBuilder.aUser

class PasswordControllerSpec extends AbstractControllerSpec {

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenGenerator generator;


    def "should update password for a user by token"() {
        given:
        User user = repository.save(aUser { token "123" });

        when:
        doPost("/users/$user.id/password-reset/123", asJson([value: "new_password"]));

        then:
        with(repository.findByEmail(user.getPerson().getEmail()).get()) {
            new BCryptPasswordEncoder().matches("new_password", password)
        }
    }

    def "should throw exception if id invalid"() {
        given:
        def user = aUser { token "123" }
        repository.save(user)

        when:
        def exception = doPost("/users/$user.id/password-reset/WRONG-TOKEN", asJson([value: "new_password"])).resolvedException

        then:
        exception.class == TokenInvalidException.class
    }

    @Override
    def getControllerUnderTest() {
        return new PasswordController(repository, generator, Mock(EmailService))
    }
}
