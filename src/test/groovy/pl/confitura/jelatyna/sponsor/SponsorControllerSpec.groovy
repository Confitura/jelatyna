package pl.confitura.jelatyna.sponsor

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.multipart.MultipartFile
import pl.confitura.jelatyna.AbstractRestSpecification

class SponsorControllerSpec extends AbstractRestSpecification {

    @Autowired
    private SponsorController controller

    void setup() {
        path("/sponsors").forController(controller)
    }

    def "should create a sponsor"() {
        when:
        def id = postSponsor("Company 1", "GOLD", "http://company.com", "this is company 1")

        then:
        with(rest.get(id)) {
            name == "Company 1"
            url == "http://company.com"
            info == "this is company 1"
            type == "GOLD"
        }
    }

    def "should find sponsors by type"() {
        given:
        postSponsor("Company 1", "GOLD")
        postSponsor("Company 2", "GOLD")
        postSponsor("Company 3", "SILVER")

        when:
        Object[] sponsors = rest.query(type: "GOLD")

        then:
        sponsors*.name
                .containsAll("Company 1", "Company 2")
    }

    def "should find all sponsors"() {
        given:
        postSponsor("Company 1", "GOLD")
        postSponsor("Company 2", "SILVER")
        postSponsor("Company 3", "BROWN")

        when:
        Object[] sponsors = rest.query()

        then:
        sponsors.length == 3
        sponsors*.name
                .containsAll("Company 1", "Company 2", "Company 3")
    }

    def "should upload logo for a sponsor"() {
        given:
        MultipartFile file = aFile()
        def id = postSponsor("Company", "GOLD")

        when:
        path("/sponsors/$id/logo").upload(file)

        then:
        rest.getAsResponse("/sponsors/$id/logo").contentAsByteArray == file.bytes

    }

    def "should update a sponsor"() {
        given:
        path("/sponsors")
        def sponsorId = postSponsor("Company 1", "GOLD", "http://company.com", "this is company 1")

        when:
        def sponsor = [
                id  : sponsorId,
                name: "Company 2",
                url : "http://company2.com",
                info: "this is company 2",
                type: "SILVER"
        ]
        rest.post(sponsor)

        then:
        with(rest.get(sponsorId)) {
            name == "Company 2"
            url == "http://company2.com"
            info == "this is company 2"
            type == "SILVER"
        }
    }

    def "should delete sponsor"() {
        given:
        def toDelete = postSponsor("Company 1", "GOLD")
        def id = postSponsor("Company 2", "GOLD")

        when:
        rest.delete(toDelete)

        then:
        rest.query()*.id == [id]

    }

    String postSponsor(String aName, String aType, String aUrl = "", String aInfo = "") {
        return rest.post(
                name: aName,
                type: aType,
                url: aUrl,
                info: aInfo
        ).id
    }

}
