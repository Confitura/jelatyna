package pl.confitura.jelatyna.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.confitura.jelatyna.email.dto.EmailDto;
import pl.confitura.jelatyna.email.dto.TemplateDto;
import pl.confitura.jelatyna.email.service.EmailService;

import java.util.List;

@RestController
@RequestMapping("/api/email")
@PreAuthorize("hasRole('ADMIN')")
public class EmailController {

    private EmailService service;

    @Autowired
    public EmailController(EmailService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.POST)

    public HttpStatus send(@RequestBody EmailDto email) {
        service.send(email);
        return HttpStatus.ACCEPTED;
    }

    @RequestMapping("/templates")
    public List<TemplateDto> getTemplates() {
        return service.getTemplates();
    }

}
