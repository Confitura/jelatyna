package pl.confitura.jelatyna.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.confitura.jelatyna.user.domain.Person;
import pl.confitura.jelatyna.user.domain.User;

@Service
@Slf4j
public class EmailService {

    private EmailSender sender;

    @Autowired
    public EmailService(EmailSender sender) {
        this.sender = sender;
    }

    public void adminCreated(Person person) {
        doSend(person, "admin-creation");
    }


    public void passwordResetRequested(User user) {
        doSend(user.getPerson(), "password-reset-requested");
    }

    private void doSend(Person person, String templateId) {
        sender.send(person.getEmail(), templateId,
            new EmailParams().firstName(person.getFirstName()).lastName(person.getLastName()).token(person.getToken()));
    }
}
