package pl.confitura.jelatyna.email;

import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage.MergeVar;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage.MergeVarBucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.google.common.collect.Lists.newArrayList;

@Service
@Profile("default")
public class MandrilSender implements EmailSender {


    private MandrillApi api;

    @Autowired
    public MandrilSender(@Value("${mandril.key}") String key) {
        api = new MandrillApi(key);
    }

    @Override
    public void send(String address, String templateId, EmailParams params) {
        try {
            doSend(address, templateId, params);
        } catch (Exception ex) {
            throw new RuntimeException("Exception while sending email", ex);
        }
    }

    private void doSend(String address, String templateId, EmailParams params) throws MandrillApiError, IOException {
        MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
        recipient.setEmail(address);
        recipient.setName(params.getFullName());

        MandrillMessage message = new MandrillMessage();
        message.setTo(newArrayList(recipient));
        message.setMergeVars(newArrayList(generateVarsBucketFor(address, params)));
        api.messages().sendTemplate(templateId, null, message, true);
    }

    private MergeVarBucket generateVarsBucketFor(String address, EmailParams params) {
        MergeVarBucket bucket = new MergeVarBucket();
        bucket.setRcpt(address);
        bucket.setVars(generateVarsFrom(params));
        return bucket;
    }

    private MergeVar[] generateVarsFrom(EmailParams params) {
        return params.asMap().entrySet().stream()
            .map(entry -> new MergeVar(entry.getKey(), entry.getValue()))
            .toArray(MergeVar[]::new);
    }
}
