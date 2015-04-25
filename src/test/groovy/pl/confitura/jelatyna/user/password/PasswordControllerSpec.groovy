package pl.confitura.jelatyna.user.password

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import pl.confitura.jelatyna.AbstractControllerSpec
import pl.confitura.jelatyna.user.TokenGenerator
import pl.confitura.jelatyna.user.TokenInvalidException
import pl.confitura.jelatyna.user.UserRepository

import static pl.confitura.jelatyna.user.UserBuilder.aUser

class PasswordControllerSpec extends AbstractControllerSpec {

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenGenerator generator;


    def "should update password for a user by token"() {
        given:
        def user = aUser {
            token "123"
        }
        repository.save(user);

        when:
        doPost("/api/password", asJson([token: "123", value: "new_password"]));

        then:
        with(repository.findByToken("123").get()) {
            new BCryptPasswordEncoder().matches("new_password", password)
        }
    }

    def "should throw exception if user doesn't exist for token"() {
        given:
        def user = aUser { token "123" }
        repository.save(user)

        when:
        def exception = doPost("/api/password", asJson([token: "WRONG_TOKEN", value: "new_password"])).resolvedException

        then:
        exception.class == TokenInvalidException.class
    }

    @Override
    def getControllerUnderTest() {
        return new PasswordController(repository)
    }
}
