package pl.confitura.jelatyna.user.password;

import lombok.Data;

@Data
public class PasswordRequest {

    private String token;
    private String value;
}
