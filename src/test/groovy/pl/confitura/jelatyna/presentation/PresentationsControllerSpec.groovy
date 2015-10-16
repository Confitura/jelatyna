package pl.confitura.jelatyna.presentation

import groovy.json.JsonBuilder
import org.springframework.beans.factory.annotation.Autowired
import pl.confitura.jelatyna.AbstractControllerSpec
import pl.confitura.jelatyna.user.UserBuilder
import pl.confitura.jelatyna.user.UserRepository
import pl.confitura.jelatyna.user.domain.User

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

import static pl.confitura.jelatyna.presentation.PresentationLevel.*


class PresentationsControllerSpec extends AbstractControllerSpec {
    @Autowired
    private PresentationRepository repository;

    @Autowired
    private UserRepository userRepository

    @PersistenceContext
    private EntityManager em;

    def "should add presentation to a user"() {
        given:
        def user = userRepository.save(UserBuilder.aUser({}))
        def presentation = new JsonBuilder()
        presentation(
                title: "title",
                shortDescription: "Short Description",
                description: "Description",
                level: BASIC,
                tags: ["JavaScript", "AngularJs"]
        )

        when:
        doPost("/users/$user.id/presentations", presentation.toString())

        then:
        Object[] presentations = get("/users/$user.id/presentations");
        presentations.length == 1
        with(presentations[0]) {
            it.title == presentation.content.title
            it.shortDescription == presentation.content.shortDescription
            it.description == presentation.content.description
            it.level == presentation.content.level.name()
            it.tags.containsAll(presentation.content.tags)
        }
    }

    def "should fetch all presentations"() {
        def user = aUser()
        given:
        addPresentationTo(user, "title 1")
        addPresentationTo(user, "title 2")
        addPresentationTo(aUser(), "title 3")

        when:
        Object[] all = get("/presentations")

        then:
        all.length == 3
        all
                .collect { it.title }
                .containsAll("title 1", "title 2", "title 3")
    }

    def "should find presentations by single tag"() {
        given:
        def user = aUser()
        addPresentationTo(user, "title 1", ["TDD", "JavaScript"])
        addPresentationTo(user, "title 2", ["JavaScript"])

        addPresentationTo(aUser(), "title 3", ["TDD"])

        when:
        Object[] presentations = get("/presentations?tags=TDD")

        then:
        presentations.length == 2
        presentations
                .collect { it.title }
                .containsAll("title 1", "title 3")
    }

    def "should find presentations by multiple tags"() {
        given:
        def user = aUser()
        addPresentationTo(user, "title 1", ["TDD", "JavaScript"])
        addPresentationTo(user, "title 2", ["JavaScript"])


        when:
        Object[] presentations = get("/presentations?tags=TDD,JavaScript")

        then:
        presentations.length == 1
        presentations
                .collect { it.title }
                .containsAll("title 1")
    }


    def "should find presentations by level"() {
        given:
        def user = aUser()
        addPresentationTo(user, "title 1", [], BASIC)
        addPresentationTo(user, "title 2", [], ADVANCED)
        addPresentationTo(user, "title 3", [], EXPERT)
        addPresentationTo(user, "title 4", [], BASIC)

        when:
        Object[] presentations = get("/presentations?level=BASIC")

        then:
        presentations.length == 2
        presentations
                .collect { it.title }
                .containsAll("title 1", "title 4")
    }

    def "should find presentations by tag and level"() {
        given:
        def user = aUser()
        addPresentationTo(user, "title 1", ["java"], BASIC)
        addPresentationTo(user, "title 2", ["TDD"], BASIC)
        addPresentationTo(user, "title 3", ["java"], EXPERT)

        when:
        Object[] presentations = get("/presentations?level=BASIC&tags=java")

        then:
        presentations.length == 1
        presentations
                .collect { it.title }
                .containsAll("title 1")
    }

    def "should add co-speaker to presentation"() {
        given:
        def owner = aUser()
        def cospeaker = aUser()
        def id = addPresentationTo(owner, "title 1", ["java"], BASIC)

        when:
        doPost("/presentations/$id/speakers/$cospeaker.id","")

        then:
        Object[] speakers = get("/presentations/$id/speakers")
        speakers.length == 2
        speakers
                .collect { it.id }
                .containsAll(owner.id, cospeaker.id)
    }

    private String addPresentationTo(User user, String title, List<String> tags = [], PresentationLevel level = BASIC) {
        def presentation = new JsonBuilder()
        presentation(title: title, tags: tags, level: level)
        return getId(doPost("/users/$user.id/presentations", presentation.toString()))
    }

    private User aUser() {
        return userRepository.save(UserBuilder.aUser({}))
    }

    @Override
    PresentationsController getControllerUnderTest() {
        return new PresentationsController(repository, userRepository);
    }
}
