package pl.confitura.jelatyna.email;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public class EmailParams {

    private Map<String, String> params = new HashMap<>();
    private byte[] barcode;
    private String address;

    public EmailParams(String address) {
        this.address = address;
    }

    public EmailParams firstName(String firstName) {
        return put("firstName", firstName);
    }

    public EmailParams lastName(String lastName) {
        return put("lastName", lastName);
    }

    public EmailParams token(String token) {
        return put("token", token);
    }

    public String getFullName() {
        return params.get("firstName") + " " + params.get("lastName");
    }

    public Map<String, String> asMap() {
        return ImmutableMap.copyOf(params);
    }

    public String getAddress() {
        return address;
    }

    private EmailParams put(String name, String value) {
        if (value != null) {
            params.put(name, value);
        }
        return this;
    }

    public void barcode(byte[] barcode) {
        this.barcode = barcode;
    }

    public byte[] getBarcode() {
        return barcode;
    }
}
