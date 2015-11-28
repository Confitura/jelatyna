package pl.confitura.jelatyna

import org.springframework.test.web.servlet.MvcResult

class RestResult {

    private final MvcResult result

    RestResult(MvcResult result) {
        this.result = result
    }

    String getId() {
        String location = result.response.getHeader("Location")
        return location[location.lastIndexOf('/') + 1..-1]
    }

    Exception getException() {
        return result.resolvedException
    }

    int getStatus() {
        return result.response.status
    }
}
