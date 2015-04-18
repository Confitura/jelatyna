package pl.confitura.jelatyna.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.confitura.jelatyna.admin.Person;

@Service
public class EmailService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Sender sender;

    public void adminCreated(Person person) {
        sender.send(person.getEmail(), "admin-creation",
            new EmailParams().firstName(person.getFirstName()).lastName(person.getLastName()).token(person.getToken()));
    }
}
