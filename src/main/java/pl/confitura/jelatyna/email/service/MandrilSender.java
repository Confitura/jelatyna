package pl.confitura.jelatyna.email.service;

import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage.MergeVar;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage.MergeVarBucket;
import com.microtripit.mandrillapp.lutung.view.MandrillTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import pl.confitura.jelatyna.email.EmailParams;
import pl.confitura.jelatyna.email.dto.TemplateDto;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.stream;

@Service
@Profile("default")
public class MandrilSender implements EmailSender {

    private Base64.Encoder encoder = Base64.getEncoder();
    private MandrillApi api;


    @Autowired
    public MandrilSender(@Value("${mandril.key}") String key) {
        this.api = new MandrillApi(key);
    }

    @Override
    public void send(String templateId, List<EmailParams> parameters, boolean includeBarcode) {
        if (includeBarcode) {
            sendSeparateEmailsPerRecipient(templateId, parameters);
        } else {
            doSend(templateId, parameters);
        }
    }

    @Override
    public void send(String templateId, EmailParams params) {
        send(templateId, newArrayList(params), false);
    }

    @Override
    public List<TemplateDto> getTemplates() {
        try {
            return stream(api.templates().list())
                .map(this::toTemplateDto)
                .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new RuntimeException("Exception while fetching templates", ex);
        }
    }

    private TemplateDto toTemplateDto(MandrillTemplate template) {
        return new TemplateDto()
            .setCode(template.getSlug())
            .setSubject(template.getSubject())
            .setText(template.getPublishCode());
    }

    private void sendSeparateEmailsPerRecipient(String templateId, List<EmailParams> parameters) {
        parameters.stream().forEach(it -> doSend(templateId, newArrayList(it)));
    }


    private void doSend(String templateId, List<EmailParams> params) {
        try {
            MandrillMessage message = new MandrillMessage();
            message.setTo(getRecipients(params));
            message.setMergeVars(generateVarsBucketFor(params));
            message.setImages(getImages(params));
            api.messages().sendTemplate(templateId, null, message, true);
        } catch (Exception ex) {
            throw new RuntimeException("Exception while sending email", ex);
        }
    }

    private List<MandrillMessage.MessageContent> getImages(List<EmailParams> params) {
        return params.stream()
            .filter(it -> it.getBarcode() != null)
            .map(it -> {
                MandrillMessage.MessageContent image = new MandrillMessage.MessageContent();
                image.setName("barcode");
                image.setType(MediaType.IMAGE_PNG_VALUE + "; name=\"barcode.png\"");
                image.setContent(encoder.encodeToString(it.getBarcode()));
                return image;
            })
            .collect(Collectors.toList());
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
