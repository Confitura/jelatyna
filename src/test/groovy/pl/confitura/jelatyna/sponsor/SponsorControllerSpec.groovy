package pl.confitura.jelatyna.sponsor

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.multipart.MultipartFile
import pl.confitura.jelatyna.AbstractControllerSpec

class SponsorControllerSpec extends AbstractControllerSpec {

    @Autowired
    private SponsorController controller

    def "should create a sponsor"() {
        when:
        def id = postSponsor("Company 1", "GOLD", "http://company.com", "this is company 1")

        then:
        with(get("/sponsors/$id")) {
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
        Object[] sponsors = get("/sponsors?type=GOLD")

        then:
        sponsors.length == 2
        sponsors[0].name == "Company 1"
        sponsors[1].name == "Company 2"
    }

    def "should find all sponsors"() {
        given:
        postSponsor("Company 1", "GOLD")
        postSponsor("Company 2", "SILVER")
        postSponsor("Company 3", "BROWN")

        when:
        Object[] sponsors = get("/sponsors")

        then:
        sponsors.length == 3
        sponsors[0].name == "Company 1"
        sponsors[1].name == "Company 2"
        sponsors[2].name == "Company 3"
    }

    def "should upload logo for a sponsor"() {
        given:
        MultipartFile file = aFile()
        def id = postSponsor("Company", "GOLD")

        when:
        uploadFile("/sponsors/$id/logo", file)

        then:
        doGet("/sponsors/$id/logo").response.contentAsByteArray == file.bytes

    }

    def "should update a sponsor"() {
        given:
        def sponsorId = postSponsor("Company 1", "GOLD", "http://company.com", "this is company 1")

        when:
        def sponsor = SponsorBuilder.json {
            id(sponsorId)
            name("Company 2")
            url("http://company2.com")
            info("this is company 2")
            type("SILVER")
        }
        post("/sponsors", sponsor.toString()).response

        then:
        with(get("/sponsors/$sponsorId")) {
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
        delete("/sponsors/$toDelete")

        then:
        get("/sponsors").collect {it.id} == [id]

    }

    String postSponsor(String aName, String aType, String aUrl = "", String aInfo = "") {
        def sponsor = SponsorBuilder.json {
            name(aName)
            type(aType)
            url(aUrl)
            info(aInfo)
        }

        return getId(post("/sponsors", sponsor.toString()))
    }

    @Override
    SponsorController getControllerUnderTest() {
        return controller;
    }
}
