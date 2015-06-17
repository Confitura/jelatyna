package pl.confitura.jelatyna.user.password;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PasswordRequest {

    @NotNull
    private String token;
    @NotNull
    private String value;
}
