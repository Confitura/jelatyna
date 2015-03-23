package pl.confitura.jelatyna.email.fake;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;
import pl.confitura.jelatyna.email.EmailPersonalizationData;
import pl.confitura.jelatyna.email.MandrilEmailSender;
import pl.confitura.jelatyna.email.EmailSender;

@Service
@Slf4j
@ConditionalOnMissingBean(MandrilEmailSender.class)
public class FakeEmailSender implements EmailSender {

    @Override
    public void send(String address, String templateId, EmailPersonalizationData params) {
        logger.info("********************************************************************");
        logger.info("Sending template email {} to {} with parameters", templateId, address);
        params.asMap()
            .forEach((key, value) -> logger.info("{} = {}", key, value));
        logger.info("********************************************************************");
    }
}
