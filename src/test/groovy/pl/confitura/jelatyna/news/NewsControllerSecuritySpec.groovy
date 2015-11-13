package pl.confitura.jelatyna.news

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import pl.confitura.jelatyna.Application
import pl.confitura.jelatyna.RestBuilder
import pl.confitura.jelatyna.security.SecurityConfiguration
import spock.lang.Specification
import spock.lang.Unroll

import javax.transaction.Transactional

import static pl.confitura.jelatyna.user.domain.Role.*

@ContextConfiguration(loader = SpringApplicationContextLoader, classes = [SecurityConfiguration, Application])
@WebAppConfiguration
@Transactional
@Rollback
@ActiveProfiles("test")
class NewsControllerSecuritySpec extends Specification {

    @Autowired
    RestBuilder rest;

    def setup() {
        rest.path("/news").full()
    }

    @Unroll()
    def "only admin should be able to create news"() {
        given:
        def news = ["title": "news 1"]

        when:
        rest.asUser(role).post(news)

        then:
        rest.count() == size

        where:
        role      || size
        ADMIN     || 1
        SPEAKER   || 0
        VOLUNTEER || 0
    }


    @Unroll()
    def "only admin should be able to delete news"() {
        given:
        def news = ["title": "news 1"]
        rest.asUser(ADMIN).post(news)

        when:
        rest.asUser(role).delete("news+1")

        then:
        rest.count() == size

        where:
        role      || size
        ADMIN     || 0
        SPEAKER   || 1
        VOLUNTEER || 1
    }

}
