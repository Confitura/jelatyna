package pl.confitura.jelatyna

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
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
import pl.confitura.jelatyna.user.domain.Role
import pl.confitura.jelatyna.user.domain.Person
import pl.confitura.jelatyna.user.domain.User
import spock.lang.Specification

import javax.transaction.Transactional
import java.security.Principal

@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = Application.class)
@WebAppConfiguration
@Transactional
@TransactionConfiguration(defaultRollback = true)
@ActiveProfiles("test")
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
    protected MvcResult doPatch(String url, String json) {
        mockMvc.perform(
            MockMvcRequestBuilders.patch(url)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
            .andReturn()
    }
    protected uploadFile(String url, MultipartFile file){
        mockMvc.perform(
                MockMvcRequestBuilders
                        .fileUpload(url).file(file))
                .andReturn();
    }

    protected MockMultipartFile aFile() {
        new MockMultipartFile("file", "photo.png", null, getClass().getResource("/photo.png").getBytes())
    }


    Principal logInAsAdmin() { return logInAs("admin@admin.pl", [UserPermission.ADMIN]) }

    Principal logInAsAnonymous() { return logInAs("", []) }

    def logInAs(String email, List<Role> permissions) {
        def person = new Person(email: email)
        def user = new User(person: person)
        def principal = new UsernamePasswordAuthenticationToken(user, "admin", permissions)
        SecurityContextHolder.getContext().setAuthentication(principal);
        return principal
    }

    abstract getControllerUnderTest();
}
