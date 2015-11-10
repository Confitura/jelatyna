package pl.confitura.jelatyna

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.multipart.MultipartFile
import pl.confitura.jelatyna.user.domain.Role

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.servlet.Filter

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload

@Component
class RestBuilder {
    private Role role = Role.ADMIN

    private String path

    private MockMvc mockMvc

    @Autowired
    private WebApplicationContext wac

    @Autowired
    private Filter springSecurityFilterChain

    @PersistenceContext
    private EntityManager em

    void full() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .addFilter(springSecurityFilterChain)
                .build()
    }

    void forController(Object controller) {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .addFilter(springSecurityFilterChain)
                .build()
    }


    RestBuilder path(String path) {
        this.path = path
        return this;
    }


    RestBuilder asUser(Role role) {
        this.role = role
        return this;
    }


    RestResult post(Map map) {
        return post(new JsonBuilder(map))
    }

    RestResult post(JsonBuilder json) {
        def result = mockMvc.perform(
                MockMvcRequestBuilders.post(path)
                        .content(json.toString())
                        .contentType(APPLICATION_JSON)
                        .with(aUser(role)))
                .andReturn()
        clear()
        return new RestResult(result)
    }


    MvcResult delete(String id) {
        return mockMvc
                .perform(
                MockMvcRequestBuilders.delete(path + "/" + id)
                        .with(aUser(role)))
                .andReturn()
    }

    void clear() {
        em.flush()
        em.clear()
    }

    int count() {
        return query().length
    }

    Object[] query() {
        return query(null)
    }

    Object[] query(Map paramMap) {
        return doGet(path + createParams(paramMap)) as Object[]
    }

    Object get(String id) {
        return doGet(path + "/" + id)
    }

    MockHttpServletResponse getAsResponse(String url) {
        return mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andReturn().response
    }

    MvcResult upload(MultipartFile aFile) {
        return mockMvc.perform(fileUpload(path).file(aFile)).andReturn()
    }


    private SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor aUser(Role role) {
        return user("test").roles(role.name())
    }

    private Serializable createParams(Map paramMap) {
        if (paramMap == null) {
            return ""
        } else {
            return paramMap
                    .inject([]) { list, k, v -> list + "$k=$v" }
                    .inject("?") { result, item -> "$result$item&" }
        }
    }

    private Object doGet(String url) {
        new JsonSlurper().parseText(getAsResponse(url).contentAsString)
    }

}
