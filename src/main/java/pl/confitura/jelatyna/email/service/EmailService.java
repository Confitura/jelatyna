package pl.confitura.jelatyna.email.service;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import pl.confitura.jelatyna.barcode.BarCodeGenerator;
import pl.confitura.jelatyna.email.EmailParams;
import pl.confitura.jelatyna.email.dto.Audience;
import pl.confitura.jelatyna.email.dto.EmailDto;
import pl.confitura.jelatyna.email.dto.TemplateDto;
import pl.confitura.jelatyna.user.PersonRepository;
import pl.confitura.jelatyna.user.domain.Person;
import pl.confitura.jelatyna.user.domain.User;

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

    public void adminCreated(Person person) {
        doSend(person, "admin-creation");
    }

    public void passwordResetRequested(User user) {
        doSend(user.getPerson(), "password-reset-requested");
    }

    public List<TemplateDto> getTemplates() {
        return sender.getTemplates();
    }

    @Transactional
    public void send(EmailDto email) {
        List<Person> people = getPeopleFor(email);
        sender.send(email.getTemplate(), getParametersFor(people, email.isIncludeBarcode()), email.isIncludeBarcode());
        if (email.isIncludeBarcode()) {
            people.stream().forEach(Person::ticketSent);
        }
    }

    private List<EmailParams> getParametersFor(List<Person> people, boolean includeBarcode) {
        return people.stream()
                .map(person -> getParametersFor(person, includeBarcode))
                .collect(toList());
    }

    private List<Person> getPeopleFor(EmailDto email) {
        Stream<Person> all = personRepository.findAll().stream();
        if (email.getAudience() == Audience.REGISTERED) {
            all = all.filter(Person::isRegistered);
        } else if (email.getAudience() == Audience.ATTENDED) {
            all = all.filter(Person::isArrived);
        }
        return all.collect(Collectors.toList());
    }

    private void doSend(Person person, String templateId) {
        sender.send(templateId, getParametersFor(person, false));
    }

    private EmailParams getParametersFor(Person person, boolean includeBarcode) {
        EmailParams params = new EmailParams(person.getEmail())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .token(person.getToken());
        if (includeBarcode) {
            params.barcode(generator.generateFor(person.getToken()));
        }
        return params;
    }

}
