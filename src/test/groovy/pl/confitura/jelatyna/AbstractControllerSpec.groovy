package pl.confitura.jelatyna

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import javax.transaction.Transactional

@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = Application.class)
@WebAppConfiguration
@Transactional
@TransactionConfiguration(defaultRollback = true)
abstract class AbstractControllerSpec extends Specification {

    MockMvc mockMvc

    @Autowired
    WebApplicationContext wac

    def setup() {
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(getControllerUnderTest())
            .build()
    }

    def asJson(MockHttpServletResponse response) {
        new JsonSlurper().parseText(response.contentAsString)
    }
    def asJson(Object content) {
        new JsonBuilder(content).toString()
    }

    protected Object doGetResponse(String url) {
        asJson(doGet(url).response)
    }
    protected MvcResult doGet(String url) {
        mockMvc.perform(MockMvcRequestBuilders.get(url)).andReturn()
    }

    protected MvcResult doPost(String url, String json) {
        mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
            .andReturn()
    }

    protected MvcResult doPut(String url, String json) {
        mockMvc.perform(
            MockMvcRequestBuilders.put(url)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
            .andReturn()
    }

    abstract getControllerUnderTest();
}
