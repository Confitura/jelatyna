package pl.confitura.jelatyna.email;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.confitura.jelatyna.admin.Admin;

import static com.google.common.collect.ImmutableMap.of;

@Service
public class EmailSender {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Sender sender;

    public void adminCreated(Admin admin) {
        logger.info("Admin created!");
        sender.send(admin.getEmail(), "admin-creation",
            of("firstName", admin.getFirstName(), "lastName", admin.getLastName(), "token", admin.getToken()));
    }
}
