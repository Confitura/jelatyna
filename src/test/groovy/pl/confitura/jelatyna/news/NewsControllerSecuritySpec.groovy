package pl.confitura.jelatyna.news

import pl.confitura.jelatyna.AbstractRestSpecification
import spock.lang.Unroll

import static pl.confitura.jelatyna.user.domain.Role.*

class NewsControllerSecuritySpec extends AbstractRestSpecification {


    def setup() {
        path("/news").full()
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
