package pl.confitura.jelatyna.email;

import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage.MergeVar;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage.MergeVarBucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.google.common.collect.Lists.newArrayList;

@Component
@Profile("default")
public class MandrilEmailSender implements EmailSender {


    private MandrillApi api;

    @Autowired
    public MandrilEmailSender(@Value("${mandril.key}") String key) {
        api = new MandrillApi(key);
    }

    @Override
    public void send(String address, String templateId, EmailPersonalizationData params) {
        try {
            doSend(address, templateId, params);
        } catch (Exception ex) {
            throw new RuntimeException("Exception while sending email", ex);
        }
    }

    private void doSend(String address, String templateId, EmailPersonalizationData params) throws MandrillApiError, IOException {
        MandrillMessage.Recipient recipient = createRecipient(address, params);
        MandrillMessage message = createMessage(address, params, recipient);
        sendMessage(templateId, message);
    }

    private MandrillMessage.Recipient createRecipient(String address, EmailPersonalizationData params) {
        MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
        recipient.setEmail(address);
        recipient.setName(params.getFullName());
        return recipient;
    }

    private MandrillMessage createMessage(String address, EmailPersonalizationData params, MandrillMessage.Recipient recipient) {
        MandrillMessage message = new MandrillMessage();
        message.setTo(newArrayList(recipient));
        message.setMergeVars(newArrayList(generateVarsBucketFor(address, params)));
        return message;
    }

    private void sendMessage(String templateId, MandrillMessage message) throws MandrillApiError, IOException {
        api.messages().sendTemplate(templateId, null, message, true);
    }

    private MergeVarBucket generateVarsBucketFor(String address, EmailPersonalizationData params) {
        MergeVarBucket bucket = new MergeVarBucket();
        bucket.setRcpt(address);
        bucket.setVars(generateVarsFrom(params));
        return bucket;
    }

    private MergeVar[] generateVarsFrom(EmailPersonalizationData params) {
        return params.asMap().entrySet().stream()
            .map(entry -> new MergeVar(entry.getKey(), entry.getValue()))
            .toArray(MergeVar[]::new);
    }
}
