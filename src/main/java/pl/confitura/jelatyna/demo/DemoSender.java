package pl.confitura.jelatyna.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.confitura.jelatyna.email.EmailParams;
import pl.confitura.jelatyna.email.Sender;

@Service
@Profile("demo")
public class DemoSender implements Sender {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void send(String address, String templateId, EmailParams params) {
        logger.info("********************************************************************");
        logger.info("Sending template email {} to {} with parameters", templateId, address);
        params.asMap().entrySet().stream()
            .forEach(entry -> logger.info("{} = {}", entry.getKey(), entry.getValue()));
        logger.info("********************************************************************");
    }
}
