package pl.confitura.jelatyna.repository

import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import pl.confitura.jelatyna.AbstractRestSpec

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ParticipantRepositorySpec extends AbstractRestSpec {

	@Test
	def "should create participant with given email"() {
		given:
		    def email = "michal@jelatyna.pl"
			mockMvc.perform(
					post("/participants").contentType(MediaType.APPLICATION_JSON).content('{"email": "'+email+'"}'))
					.andExpect(status().isCreated())

		when:
			MockHttpServletResponse response = mockMvc.perform(get("/participants")).andReturn().response

		then:
			response.status == HttpStatus.OK.value()
			asJson(response)._embedded.participants[0].email == email
	}
}
