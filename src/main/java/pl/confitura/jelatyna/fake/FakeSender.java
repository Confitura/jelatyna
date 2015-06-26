package pl.confitura.jelatyna.fake;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.confitura.jelatyna.email.EmailParams;
import pl.confitura.jelatyna.email.service.EmailSender;
import pl.confitura.jelatyna.email.dto.TemplateDto;

import java.util.ArrayList;
import java.util.List;

@Service
@Profile({"fake", "test"})
@Slf4j
public class FakeSender implements EmailSender {

    @Override
    public void send(String templateId, EmailParams params) {
        log.info("********************************************************************");
        log.info("Sending template email {} to {} with parameters", templateId, params.getAddress());
        params.asMap()
            .forEach((key, value) -> log.info("{} = {}", key, value));
        log.info("********************************************************************");
    }

    @Override
    public void send(String templateId, List<EmailParams> parameters, boolean includeBarcode) {
        parameters.stream().forEach(it -> this.send(templateId, it));
    }

    @Override
    public List<TemplateDto> getTemplates() {
        List<TemplateDto> templates = new ArrayList<>();
        templates.add(new TemplateDto()
            .setCode("tempate-1")
            .setSubject("Subject 1")
            .setText("This is body of the template"));
        templates.add(new TemplateDto()
            .setCode("tempate-2")
            .setSubject("Subject 2")
            .setText("Natenczas Wojski chwycił na taśmie przypięty swój róg bawoli."));
        return templates;
    }
}
