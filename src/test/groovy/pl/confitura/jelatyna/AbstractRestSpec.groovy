package pl.confitura.jelatyna

import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import pl.confitura.jelatyna.domain.Participant
import pl.confitura.jelatyna.domain.User
import pl.confitura.jelatyna.domain.UserPermission
import spock.lang.Specification

import java.security.Principal

@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = Application.class)
@WebAppConfiguration
class AbstractRestSpec extends Specification {

	MockMvc mockMvc

	@Autowired
	WebApplicationContext wac

	def setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build()
	}

	def asJson(MockHttpServletResponse response) {
		new JsonSlurper().parseText(response.contentAsString)
	}

	Principal logInAsAdmin() { return logInAs("admin@admin.pl", [UserPermission.ADMIN]) }

	def logInAs(String email, ArrayList<UserPermission> permissions) {
		def participant = new Participant(email: email)
		def user = new User(participant: participant)
		def principal = new UsernamePasswordAuthenticationToken(user, "admin", permissions)
		SecurityContextHolder.getContext().setAuthentication(principal);
		return principal
	}
}
