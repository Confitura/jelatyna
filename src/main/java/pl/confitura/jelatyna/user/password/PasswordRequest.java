package pl.confitura.jelatyna.user.password;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PasswordRequest {

    @NotNull
    private String value;
}
