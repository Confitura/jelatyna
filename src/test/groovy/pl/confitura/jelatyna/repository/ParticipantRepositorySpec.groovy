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

class ParticipantRepositorySpec extends AbstractRestSpec  {

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
}
