package pl.confitura.jelatyna.fake;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.confitura.jelatyna.email.EmailParams;
import pl.confitura.jelatyna.email.Sender;

@Service
@Profile("demo")
public class FakeSender implements Sender {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void send(String address, String templateId, EmailParams params) {
        logger.info("********************************************************************");
        logger.info("Sending template email {} to {} with parameters", templateId, address);
        params.asMap()
            .forEach((key, value) -> logger.info("{} = {}", key, value));
        logger.info("********************************************************************");
    }
}
