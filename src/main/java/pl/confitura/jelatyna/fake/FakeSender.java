package pl.confitura.jelatyna.fake;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.confitura.jelatyna.email.EmailParams;
import pl.confitura.jelatyna.email.EmailSender;

@Service
@Profile("fake")
@Slf4j
public class FakeSender implements EmailSender {

    @Override
    public void send(String address, String templateId, EmailParams params) {
        log.info("********************************************************************");
        log.info("Sending template email {} to {} with parameters", templateId, address);
        params.asMap()
            .forEach((key, value) -> log.info("{} = {}", key, value));
        log.info("********************************************************************");
    }
}
