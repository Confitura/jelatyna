package pl.confitura.jelatyna

import org.springframework.test.web.servlet.setup.MockMvcBuilders

abstract class AbstractControllerSpec extends AbstractRestSpec {

    def setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controllerUnderTest)
                .build()
    }

    abstract getControllerUnderTest()
}
