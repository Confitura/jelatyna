package pl.confitura.jelatyna.email.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TemplateDto {
    private String code;
    private String subject;
    private String text;

}
