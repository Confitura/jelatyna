package pl.confitura.jelatyna;

import static java.time.LocalDateTime.ofInstant;
import static java.time.ZoneId.systemDefault;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EntityScan(
        basePackageClasses = {Application.class}
)
@SpringBootApplication
@EnableSwagger2
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    public ObjectMapper jacksonObjectMapper() {
//        return Jackson2ObjectMapperBuilder.json()
//                .serializationInclusion(JsonInclude.Include.NON_NULL) // Don’t include null values
//                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) //ISODate
//                .modules(new JSR310Module())
//                .build();
//    }

//    @Bean
//    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
//        return new Jackson2ObjectMapperBuilder()
//                .json()
//                .modules(new JSR310Module());
//
//    }

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
