package pl.confitura.jelatyna.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.confitura.jelatyna.admin.Admin;

import static com.google.common.collect.ImmutableMap.of;

@Service
public class EmailService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Sender sender;

    public void adminCreated(Admin admin) {
        sender.send(admin.getEmail(), "admin-creation",
            new EmailParams().firstName(admin.getFirstName()).lastName(admin.getLastName()).token(admin.getToken()));
    }
}
