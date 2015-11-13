package pl.confitura.jelatyna.news
import org.springframework.beans.factory.annotation.Autowired
import pl.confitura.jelatyna.AbstractRestSpecification
import pl.confitura.jelatyna.user.UserRepository


class NewsControllerSpec extends AbstractRestSpecification {
    @Autowired
    private NewsController controller

    @Autowired
    private UserRepository userRepository;

    void setup() {
        rest.path("/news").forController(controller)
    }

    def "should save news"() {
        given:
        def user = userRepository.save(aUser({}))

        when:
        saveNews(
                title: "Title",
                published: false,
                authot: user.id,
                publicationDate: "2015-01-01T01:02",
                shortText: "Short text",
                longText: "Long text"
        )

        then:
        Object[] fetched = rest.query()
        fetched.length == 1
        with(fetched[0]) {
            it.id != null
            it.title == "Title"
            it.published == false
//            it.publicationDate == "2015-01-01T01:02"
            it.shortText == "Short text"
            it.longText == "Long text"
        }
    }

    def "should update news"() {
        given:
        def user = userRepository.save(aUser({}))
        def id = rest.post([
                title          : "Title",
                published      : false,
                authot         : user.id,
                publicationDate: "2015-01-01T01:02",
                shortText      : "Short text",
                longText       : "Long text"]).getId()

        when:
        saveNews(
                id: id,
                title: "Other title",
                published: true,
                authot: user.id,
                publicationDate: "2015-01-01T01:02",
                shortText: "Short text",
                longText: "Long text"
        )


        then:
        Object[] fetched = rest.query()
        fetched.length == 1
        with(fetched[0]) {
            it.title == "Other title"
            it.published == true
//            it.publicationDate == "2015-01-01T01:02"
            it.shortText == "Short text"
            it.longText == "Long text"
        }
    }

    def "should fetch news by id"() {
        given:
        def id = saveNews(title: "Title")
        saveNews(title: "Title 2")

        when:
        def fetched = rest.get("$id")

        then:
        fetched.title == "Title"
    }

    def "should create news with id as simplified title"() {

        when:
        def id = saveNews(title: "This is my title")

        then:
        id == "This+is+my+title"
    }

    def "should update id when updating title"() {
        given:
        def id = saveNews(title: "title")

        when:
        def updatedId = saveNews(id: id, title: "different title")

        then:
        updatedId == "different+title"
    }

    def "should delete news"() {
        given:
        def id = saveNews(title: "title 1")
        saveNews(title: "title 2")

        when:
        rest.delete(id)

        then:
        Object[] news = rest.query()
        news.length == 1
        news[0].title == "title 2"
    }

    def "should fetch page of news"() {
        given:
        saveNews(title: "title 1")
        saveNews(title: "title 2")
        saveNews(title: "title 3")
        saveNews(title: "title 4")

        when:
        Object[] fetched = rest.query(offset: 1, limit: 2)

        then:
        fetched.length == 2
        fetched.collect { it.title }.containsAll(["title 2", "title 3"])
    }

    private String saveNews(Map values) {
        return rest.post(values).getId()
    }

}
