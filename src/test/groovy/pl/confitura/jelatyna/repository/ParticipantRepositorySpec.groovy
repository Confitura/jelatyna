package pl.confitura.jelatyna.repository

import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import pl.confitura.jelatyna.AbstractRestSpec

import java.security.Principal

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ParticipantRepositorySpec extends AbstractRestSpec {

    @Test
    def "should create participant with given name"() {
        given:
          logInAsAdmin()
          def postResult = mockMvc.perform(
                  post("/participants").contentType(MediaType.APPLICATION_JSON).content('{"firstName": "michal"}'))
                  .andExpect(status().isCreated()).andReturn()

        when:
          def location = postResult.getResponse().getHeader("location")
          MockHttpServletResponse response = mockMvc.perform(get(location)).andReturn().response

        then:
          response.status == HttpStatus.OK.value()
          asJson(response).firstName == "michal"
    }

    @Test
    def "should not allow anonymous to list participants"() {
        given:
          logInAsAnonymous()

        when:
          MockHttpServletResponse response = mockMvc.perform(get("/participants")).andReturn().response

        then:
          response.status == HttpStatus.FORBIDDEN.value()
    }


    @Test
    def "should allow admin to list participants"() {
        given:
          logInAsAdmin()

        when:
          MockHttpServletResponse response = mockMvc.perform(get("/participants")).andReturn().response

        then:
          response.status == HttpStatus.OK.value()
    }


    @Test
    def "should allow participant to access to his data"() {
        given:
          logInAsAnonymous()
          def email = 'michal@micha.pl';
          def postResult = mockMvc.perform(
                  post("/participants").contentType(MediaType.APPLICATION_JSON).content("""{"email": "${email}"}"""))
                  .andExpect(status().isCreated()).andReturn()

        when:
          logInAs(email,[])
          def location = postResult.getResponse().getHeader("location")
          MockHttpServletResponse response = mockMvc.perform(get(location)).andReturn().response

        then:
          response.status == HttpStatus.OK.value()
          asJson(response).email == email
    }
    
    @Test
    def "should NOT allow participant to access participants list"() {
        given:
          logInAsAnonymous()
          def email = 'dmnk@dmnk.pl';
          mockMvc.perform(
                  post("/participants").contentType(MediaType.APPLICATION_JSON).content("""{"email": "${email}"}"""))
                  .andExpect(status().isCreated()).andReturn()

        when:
          logInAs(email,[])
          MockHttpServletResponse response = mockMvc.perform(get("/participants")).andReturn().response

        then:
          response.status == HttpStatus.FORBIDDEN.value()
    }
}
