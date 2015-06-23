package pl.confitura.jelatyna.email.service;

import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage.MergeVar;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage.MergeVarBucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.confitura.jelatyna.email.EmailParams;
import pl.confitura.jelatyna.email.dto.TemplateDto;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    public void send(String templateId, List<EmailParams> parameters) {
        try {
            doSend(templateId, parameters);
        } catch (Exception ex) {
            throw new RuntimeException("Exception while sending email", ex);
        }
    }

    @Override
    public void send(String templateId, EmailParams params) {
        send(templateId, newArrayList(params));
    }

    @Override
    public List<TemplateDto> getTemplates() {
        try {
            return Arrays.stream(api.templates().list())
                .map(template -> new TemplateDto()
                    .setCode(template.getSlug())
                    .setSubject(template.getSubject())
                    .setText(template.getPublishCode()))
                .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new RuntimeException("Exception while fetching templates", ex);
        }
    }


    private void doSend(String templateId, List<EmailParams> params) throws MandrillApiError, IOException {
        MandrillMessage message = new MandrillMessage();
        message.setTo(getRecipients(params));
        message.setMergeVars(generateVarsBucketFor(params));
//        message.setImages();
        api.messages().sendTemplate(templateId, null, message, true);
    }

    private List<MergeVarBucket> generateVarsBucketFor(List<EmailParams> params) {
        return params.stream()
            .map(this::generateVarsBucketFor)
            .collect(Collectors.toList());
    }

    private List<MandrillMessage.Recipient> getRecipients(List<EmailParams> params) {
        return params.stream()
            .map(this::getRecipient)
            .collect(Collectors.toList());
    }

    private MandrillMessage.Recipient getRecipient(EmailParams params) {
        MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
        recipient.setName(params.getFullName());
        recipient.setEmail(params.getAddress());
        return recipient;
    }

    private MergeVarBucket generateVarsBucketFor(EmailParams params) {
        MergeVarBucket bucket = new MergeVarBucket();
        bucket.setRcpt(params.getAddress());
        bucket.setVars(generateVarsFrom(params));
        return bucket;
    }

    private MergeVar[] generateVarsFrom(EmailParams params) {
        return params.asMap().entrySet().stream()
            .map(entry -> new MergeVar(entry.getKey(), entry.getValue()))
            .toArray(MergeVar[]::new);
    }
}
