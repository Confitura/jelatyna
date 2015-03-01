package pl.confitura.jelatyna.email;

public interface Sender {

    void send(String address, String templateId, EmailParams params);
}
