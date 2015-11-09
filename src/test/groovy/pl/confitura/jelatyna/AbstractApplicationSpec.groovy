package pl.confitura.jelatyna

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import javax.servlet.Filter

class AbstractApplicationSpec extends AbstractRestSpec {
    @Autowired
    private Filter springSecurityFilterChain


    def setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .addFilter(springSecurityFilterChain)
                .build();
    }

}
