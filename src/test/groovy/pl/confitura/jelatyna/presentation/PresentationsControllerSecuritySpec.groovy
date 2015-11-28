package pl.confitura.jelatyna.presentation

import org.springframework.beans.factory.annotation.Autowired
import pl.confitura.jelatyna.AbstractRestSpecification
import pl.confitura.jelatyna.user.UserBuilder
import pl.confitura.jelatyna.user.UserRepository
import pl.confitura.jelatyna.user.domain.User

import static org.apache.http.HttpStatus.SC_FORBIDDEN
import static pl.confitura.jelatyna.user.domain.Role.ADMIN
import static pl.confitura.jelatyna.user.domain.Role.SPEAKER

class PresentationsControllerSecuritySpec extends AbstractRestSpecification {

    @Autowired
    private UserRepository userRepository

    void setup() {
        rest.full()
    }

    def "owner should be able to create new presentation"() {
        given:
        User user = userRepository.save(UserBuilder.aUser {})
        Object presentation = [title: "title"]

        when:
        rest.path("/users/${user.id}/presentations").asUser(SPEAKER, user.id).post(presentation)

        then:
        rest.count() == 1
    }

    def "speaker should not be able to create new presentation for different speaker"() {
        given:
        User speaker = userRepository.save(UserBuilder.aUser {})
        User owner = userRepository.save(UserBuilder.aUser {})
        def presentation = [title: "title"]

        when:
        def status = rest.path("/users/${owner.id}/presentations").asUser(SPEAKER, speaker.id).post(presentation).status

        then:

        status == SC_FORBIDDEN
    }

    def "admin should be able to create a presentation for any user"() {
        given:
        User owner = userRepository.save(UserBuilder.aUser {})
        User admin = userRepository.save(UserBuilder.aUser {})
        Object presentation = [title: "title"]

        when:
        rest.path("/users/${owner.id}/presentations").asUser(ADMIN, admin.id).post(presentation)

        then:
        rest.count() == 1
    }

}
