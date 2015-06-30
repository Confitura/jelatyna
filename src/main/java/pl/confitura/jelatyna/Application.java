package pl.confitura.jelatyna;

import static java.time.LocalDateTime.*;
import static java.time.ZoneId.*;

import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;

@EntityScan(
        basePackageClasses = {Application.class}
)
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Converter(autoApply = true)
    public static class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Date> {

        @Override
        public Date convertToDatabaseColumn(LocalDateTime date) {
            return date == null ? null : Date.from(date.atZone(systemDefault()).toInstant());
        }

        @Override
        public LocalDateTime convertToEntityAttribute(Date date) {
            return date == null ? null : ofInstant(date.toInstant(), systemDefault());
        }
    }


}
