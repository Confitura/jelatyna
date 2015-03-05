package pl.confitura.jelatyna.participant
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import pl.confitura.jelatyna.AbstractControllerSpec

class ControllerSpec extends AbstractControllerSpec {
    @Autowired
    Controller controller;

    @Test
    def "should create participant with given name"() {
        given:
          doPost("/api/participant", '{"firstName": "michal"}');

        when:

          def participants = doGet("/api/participant")

        then:
          participants[0].firstName == "michal"
    }

    @Override
    def getControllerUnderTest() {
        return controller
    }
}
