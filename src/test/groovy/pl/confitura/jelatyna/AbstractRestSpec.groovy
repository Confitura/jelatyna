package pl.confitura.jelatyna

import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

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
}
