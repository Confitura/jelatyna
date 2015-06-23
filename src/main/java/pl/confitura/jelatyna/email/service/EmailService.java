package pl.confitura.jelatyna.email.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.confitura.jelatyna.barcode.BarCodeGenerator;
import pl.confitura.jelatyna.email.EmailParams;
import pl.confitura.jelatyna.email.dto.EmailDto;
import pl.confitura.jelatyna.email.dto.TemplateDto;
import pl.confitura.jelatyna.user.PersonRepository;
import pl.confitura.jelatyna.user.domain.Person;
import pl.confitura.jelatyna.user.domain.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmailService {

    private EmailSender sender;
    private PersonRepository personRepository;

    private BarCodeGenerator generator;

    @Autowired
    public EmailService(EmailSender sender, PersonRepository personRepository, BarCodeGenerator generator) {
        this.sender = sender;
        this.personRepository = personRepository;
        this.generator = generator;
    }

    public void send(EmailDto email) {
        List<Person> people = getPeopleFor(email);
        sender.send(email.getTemplate(), getParametersFor(people));
    }

    private List<EmailParams> getParametersFor(List<Person> people) {
        return people.stream()
            .map(it -> {
                    EmailParams params = getParametersFor(it);
                    params.addImage("barcode", generator.generateFor(it.getToken()));
                    return params;
                }
            )
            .collect(Collectors.toList());
    }


    private List<Person> getPeopleFor(EmailDto email) {
        if ("all".equals(email.getAudience())) {
            return personRepository.findAll();
        } else {
            return personRepository.findAllRegistered();
        }
    }


    public void adminCreated(Person person) {
        doSend(person, "admin-creation");
    }

    public void passwordResetRequested(User user) {
        doSend(user.getPerson(), "password-reset-requested");
    }

    private void doSend(Person person, String templateId) {
        sender.send(templateId,
            getParametersFor(person));
    }

    private EmailParams getParametersFor(Person person) {
        return new EmailParams(person.getEmail())
            .firstName(person.getFirstName())
            .lastName(person.getLastName())
            .token(person.getToken());
    }

    public List<TemplateDto> getTemplates() {
        return sender.getTemplates();
    }
}
