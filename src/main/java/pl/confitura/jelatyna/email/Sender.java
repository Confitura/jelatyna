package pl.confitura.jelatyna.email;

import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage.MergeVar;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage.MergeVarBucket;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

@Component
public class Sender {

    public void send(String address, String templateId, Map<String, String> params) {
        try {
            MandrillApi api = new MandrillApi("ayH_KV3CVLeKIdwlZ8DxNA");
            MandrillMessage message = new MandrillMessage();

            MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
            recipient.setEmail(address);
            recipient.setName(params.get("firstName") + " " + params.get("lastName"));
            message.setTo(newArrayList(recipient));
            message.setMergeVars(newArrayList(generateVarsBucketFor(address, params)));
            api.messages().sendTemplate(templateId, null, message, true);
        } catch (Exception ex) {
            throw new RuntimeException("Exception while sending email", ex);
        }
    }

    private MergeVarBucket generateVarsBucketFor(String address, Map<String, String> params) {
        MergeVarBucket bucket = new MergeVarBucket();
        bucket.setRcpt(address);
        bucket.setVars(generateVarsFrom(params));
        return bucket;
    }

    private MergeVar[] generateVarsFrom(Map<String, String> params) {
        return params.entrySet().stream()
            .map(entry -> new MergeVar(entry.getKey(), entry.getValue()))
            .toArray(MergeVar[]::new);
    }
}
