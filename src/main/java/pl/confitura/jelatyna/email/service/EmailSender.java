package pl.confitura.jelatyna.email.service;

import pl.confitura.jelatyna.email.EmailParams;
import pl.confitura.jelatyna.email.dto.TemplateDto;

import java.util.List;

public interface EmailSender {

    void send(String templateId, EmailParams params);

    void send(String templateId, List<EmailParams> parameters);

    List<TemplateDto> getTemplates();

}
