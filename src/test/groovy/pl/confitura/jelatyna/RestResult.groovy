package pl.confitura.jelatyna

import org.springframework.test.web.servlet.MvcResult

class RestResult {

    private MvcResult result

    RestResult(MvcResult result) {
        this.result = result
    }

    public String getId() {
        def location = result.response.getHeader("Location")
        return location.substring(location.lastIndexOf('/') + 1);
    }


}
