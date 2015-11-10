package pl.confitura.jelatyna.presentation
import groovy.json.JsonBuilder
import org.springframework.beans.factory.annotation.Autowired
import pl.confitura.jelatyna.NewAbstractSpecification
import pl.confitura.jelatyna.RestBuilder
import pl.confitura.jelatyna.user.UserBuilder
import pl.confitura.jelatyna.user.UserRepository
import pl.confitura.jelatyna.user.domain.User

import static pl.confitura.jelatyna.presentation.PresentationLevel.*

class PresentationsControllerSpec extends NewAbstractSpecification {
    @Autowired
    private PresentationRepository repository

    @Autowired
    private UserRepository userRepository

    void setup() {
        rest.forController(new PresentationsController(repository, userRepository))
    }

    def "should add presentation to a user"() {
        given:
        def user = userRepository.save(UserBuilder.aUser({}))
        def presentation = [title           : "title",
                            language        : "PL",
                            shortDescription: "Short Description",
                            description     : "Description",
                            level           : BASIC,
                            tags            : ["JavaScript", "AngularJs"]
        ]

        when:
        path("/users/$user.id/presentations").post(presentation)

        then:
        Object[] presentations = rest.query()
        presentations.length == 1
        with(presentations[0]) {
            it.title == presentation.title
            it.language == presentation.language
            it.shortDescription == presentation.shortDescription
            it.description == presentation.description
            it.level == presentation.level.name()
            it.tags.containsAll(presentation.tags)
        }
    }

    def "should update presentation"() {
        given:
        def user = aUser()
        def presentation = [
                title           : "title",
                language        : "PL",
                shortDescription: "Short Description",
                description     : "Description",
                level           : BASIC,
                tags            : ["JavaScript", "AngularJs"]
        ]
        path("/users/$user.id/presentations")
        def presentationId = rest.post(presentation).getId()
        presentation.id = presentationId;
        presentation.title = "Other title"

        when:
        rest.post(presentation)

        then:
        Object[] presentations = rest.query()
        presentations.length == 1
        with(presentations[0]) {
            it.title == "Other title"
            it.language == "PL"
        }
    }

    def "should fetch all presentations"() {
        given:
        def user = aUser()
        addPresentationTo(user, "title 1")
        addPresentationTo(user, "title 2")
        addPresentationTo(aUser(), "title 3")

        when:
        Object[] presentations = presentations().query()

        then:
        presentations.length == 3
        presentations
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
        Object[] presentations = presentations().query(tags: "TDD")

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
        Object[] presentations = rest.path("/presentations").query(tags: "TDD,JavaScript")

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
        Object[] presentations = presentations().query(level: "BASIC")

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
        Object[] presentations = presentations().query(level: "BASIC", tags: "java")

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
        path("/presentations/$id/speakers/$cospeaker.id").post(new JsonBuilder())

        then:
        Object[] speakers = path("/presentations/$id/speakers").query()
        speakers.length == 2
        speakers
                .collect { it.id }
                .containsAll(owner.id, cospeaker.id)
    }


    def "should delete a presentation"() {
        given:
        def id = addPresentationTo(aUser(), "title 1", ["java"], BASIC)

        when:
        presentations().delete(id)

        then:
        rest.count() == 0
    }

    private String addPresentationTo(User user, String title, List<String> tags = [], PresentationLevel level = BASIC) {
        return path("/users/$user.id/presentations")
                .post([title: title, tags: tags, level: level])
                .getId()
    }

    private User aUser() {
        return userRepository.save(UserBuilder.aUser({}))
    }

    private RestBuilder presentations() {
        rest.path("/presentations")
    }

}
