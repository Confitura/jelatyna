package pl.confitura.jelatyna.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import pl.confitura.jelatyna.user.domain.Person;

@Service
@Slf4j
public class EmailService {


    @Autowired
    private EmailSender sender;

    public void adminCreated(Person person) {
        sender.send(person.getEmail(), "admin-creation",
            new EmailParams().firstName(person.getFirstName()).lastName(person.getLastName()).token(person.getToken()));
    }
}
