package pl.confitura.jelatyna;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.confitura.jelatyna.domain.Participant;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TestController {
    @RequestMapping("/hello/{name}")
    public List<String> hello(@PathVariable("name") String name) {
        return Arrays.asList("Hello " + name+"!");
    }
}
