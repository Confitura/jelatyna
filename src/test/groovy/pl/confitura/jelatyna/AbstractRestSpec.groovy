package pl.confitura.jelatyna

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.multipart.MultipartFile
import pl.confitura.jelatyna.security.SecurityConfiguration
import pl.confitura.jelatyna.user.domain.Role
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload
import static pl.confitura.jelatyna.user.domain.Role.ADMIN

@ContextConfiguration(loader = SpringApplicationContextLoader, classes = [SecurityConfiguration, Application])
@WebAppConfiguration
@Transactional
@Rollback
@ActiveProfiles("test")
class AbstractRestSpec extends Specification {

    MockMvc mockMvc

    @Autowired
    WebApplicationContext wac

    @PersistenceContext
    private EntityManager em

    private String path = ""


    Object asJson(MockHttpServletResponse response) {
        return new JsonSlurper().parseText(response.contentAsString)
    }

    String asJson(Object content) {
        return new JsonBuilder(content).toString()
    }

    protected MvcResult doGet(String url) {
        return mockMvc.perform(MockMvcRequestBuilders.get(url)).andReturn()
    }

    protected MvcResult doPost(String url, String json, Role role = ADMIN) {
        def result = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(json)
                        .contentType(APPLICATION_JSON)
                        .with(aUser(role)))
                .andReturn()
        clear()
        return result
    }


    protected MvcResult post(String json, Role role = ADMIN) {
        return doPost(path, json, role)
    }

    protected MvcResult doPut(String url, String json) {
        return mockMvc.perform(
                MockMvcRequestBuilders.put(url)
                        .content(json)
                        .contentType(APPLICATION_JSON))
                .andReturn()
    }

    protected MvcResult doPatch(String url, String json) {
        return mockMvc.perform(
                MockMvcRequestBuilders.patch(url)
                        .content(json)
                        .contentType(APPLICATION_JSON))
                .andReturn()
    }

    protected MvcResult delete(String url, Role role = ADMIN) {
        return mockMvc
                .perform(MockMvcRequestBuilders.delete(url)
                .with(aUser(role)))
                .andReturn()
    }

    protected MvcResult delete(Object id, Role role = ADMIN) {
        return delete(path + "/" + id, role)
    }


    protected MvcResult uploadFile(String url, MultipartFile aFile) {
        return mockMvc.perform(
                fileUpload(url).file(aFile)
        ).andReturn()
    }

    protected MockMultipartFile aFile() {
        return new MockMultipartFile("file", "photo.png", null, getClass().getResource("/photo.png").bytes)
    }

    protected String getId(MvcResult result) {
        def location = result.response.getHeader("Location")
        return location.substring(location.lastIndexOf('/') + 1);
    }

    protected Object get(String location) {
        return asJson(doGet(location).response)
    }

    protected Object[] query(String location) {
        return get(location) as Object[]
    }

    protected Object[] query() {
        return query(path)
    }

    protected void path(String path) {
        this.path = path;
    }

    void clear() {
        em.flush()
        em.clear()
    }

    private SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor aUser(Role role) {
        return user("test").roles(role.name())
    }

}
