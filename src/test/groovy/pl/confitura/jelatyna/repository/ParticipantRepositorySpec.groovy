package pl.confitura.jelatyna.repository
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import pl.confitura.jelatyna.AbstractRestSpec

class ParticipantRepositorySpec extends AbstractRestSpec {
    @Autowired
    ParticipantRepository repository;

    @Test
    def "should create participant with given name"() {
        given:
          doPost("/participants", '{"firstName": "michal"}');

        when:

          def response = mockMvc.perform(MockMvcRequestBuilders.get("/participants")).andReturn().response //doGet("/participant")

        then:
          println response.status == 200
          asJson(response)._embedded.participants[0].firstName == "michal"
    }

    @Override
    def getControllerUnderTest() {
        return repository
    }
}
