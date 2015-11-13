package pl.confitura.jelatyna.user.password

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import pl.confitura.jelatyna.AbstractRestSpecification
import pl.confitura.jelatyna.email.service.EmailService
import pl.confitura.jelatyna.user.TokenGenerator
import pl.confitura.jelatyna.user.TokenInvalidException
import pl.confitura.jelatyna.user.UserRepository
import pl.confitura.jelatyna.user.domain.User


class PasswordControllerSpec extends AbstractRestSpecification {

    @Autowired
    private UserRepository repository

    @Autowired
    private TokenGenerator generator

    void setup() {
        rest.forController(new PasswordController(repository, generator, Mock(EmailService)))
    }

    def "should update password for a user by token"() {
        given:
        User user = repository.save(aUser { token "123" })

        when:
        path("/users/$user.id/password-reset/123").post([value: "new_password"])

        then:
        with(repository.findByEmail(user.person.email).get()) {
            new BCryptPasswordEncoder().matches("new_password", password)
        }
    }

    def "should throw exception if id invalid"() {
        given:
        def user = aUser { token "123" }
        repository.save(user)

        when:
        def exception = path("/users/$user.id/password-reset/WRONG-TOKEN").post([value: "new_password"]).exception

        then:
        exception.class == TokenInvalidException
    }

}
