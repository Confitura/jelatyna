package pl.confitura.jelatyna.email;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

public class EmailParams {

    private Map<String, String> params = new HashMap<>();

    public EmailParams firstName(String firstName) {
        params.put("firstName", firstName);
        return this;
    }

    public EmailParams lastName(String lastName) {
        params.put("lastName", lastName);
        return this;
    }

    public EmailParams token(String token) {
        params.put("token", token);
        return this;
    }

    public String getFullName() {
        return params.get("firstName") + " " + params.get("lastName");
    }

    public Map<String, String> asMap() {
        return unmodifiableMap(params);
    }

}
