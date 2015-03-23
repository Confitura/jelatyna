package pl.confitura.jelatyna.email;

import pl.confitura.jelatyna.admin.Admin;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

public class EmailPersonalizationData {

    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String TOKEN = "token";

    private Map<String, String> params = new HashMap<>();

    public EmailPersonalizationData firstName(String firstName) {
        params.put(FIRST_NAME, firstName);
        return this;
    }

    public EmailPersonalizationData lastName(String lastName) {
        params.put(LAST_NAME, lastName);
        return this;
    }

    public EmailPersonalizationData token(String token) {
        params.put(TOKEN, token);
        return this;
    }

    public Map<String, String> asMap() {
        return unmodifiableMap(params);
    }

    public String getFullName() {
        return params.get(FIRST_NAME) + " " + params.get(LAST_NAME);
    }

    public static EmailPersonalizationData of(Admin admin) {
        return new EmailPersonalizationData()
                .firstName(admin.getFirstName())
                .lastName(admin.getLastName())
                .token(admin.getToken());
    }
}
