package pl.confitura.jelatyna.email.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EmailDto {

    private Audience audience;
    private boolean includeBarcode;
    private String template;

}
