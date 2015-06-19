package pl.confitura.jelatyna.email;

public interface EmailSender {

    void send(String address, String templateId, EmailParams params);

}
