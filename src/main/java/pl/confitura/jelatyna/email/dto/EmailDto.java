package pl.confitura.jelatyna.email.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EmailDto {

    private String audience;
    private boolean includeBarcode;
    private String template;

}
