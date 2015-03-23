package pl.confitura.jelatyna.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.confitura.jelatyna.admin.Admin;

@Slf4j
@Service
public class EmailService {

    public static final String ADMIN_CREATION_TEMPLET_ID = "admin-creation";

    @Autowired
    private EmailSender emailSender;

    public void notifyAdminAboutCreatedAccount(Admin admin) {
        emailSender.send(admin.getEmail(), ADMIN_CREATION_TEMPLET_ID, EmailPersonalizationData.of(admin));
    }
}
