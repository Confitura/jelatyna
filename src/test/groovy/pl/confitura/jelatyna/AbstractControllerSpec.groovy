package pl.confitura.jelatyna

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.multipart.MultipartFile
import pl.confitura.jelatyna.security.SecurityConfiguration
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

@ContextConfiguration(loader = SpringApplicationContextLoader, classes = [SecurityConfiguration, Application])
@WebAppConfiguration
@Transactional
@TransactionConfiguration(defaultRollback = true)
@ActiveProfiles("test")
abstract class AbstractControllerSpec extends Specification {

    MockMvc mockMvc

    @Autowired
    WebApplicationContext wac

    @PersistenceContext
    private EntityManager em

    def setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controllerUnderTest)
                .build()
    }

    Object asJson(MockHttpServletResponse response) {
        return new JsonSlurper().parseText(response.contentAsString)
    }

    String asJson(Object content) {
        return new JsonBuilder(content).toString()
    }

    protected MvcResult doGet(String url) {
        return mockMvc.perform(MockMvcRequestBuilders.get(url)).andReturn()
    }

    protected MvcResult doPost(String url, String json) {
        def result = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
        clear()
        return result
    }

    protected MvcResult doPut(String url, String json) {
        return mockMvc.perform(
                MockMvcRequestBuilders.put(url)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
    }

    protected MvcResult doPatch(String url, String json) {
        return mockMvc.perform(
                MockMvcRequestBuilders.patch(url)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
    }

    protected MvcResult uploadFile(String url, MultipartFile file) {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .fileUpload(url).file(file))
                .andReturn()
    }

    protected MockMultipartFile aFile() {
        return new MockMultipartFile("file", "photo.png", null, getClass().getResource("/photo.png").bytes)
    }

    protected String getId(MvcResult result){
        def location = result.response.getHeader("Location")
        return location.substring(location.lastIndexOf('/') + 1);
    }

    protected Object get(String location) {
        return asJson(doGet(location).response)
    }

    void clear() {
        em.flush()
        em.clear()
    }


    abstract getControllerUnderTest()
}
