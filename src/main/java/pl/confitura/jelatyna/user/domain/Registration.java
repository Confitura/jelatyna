package pl.confitura.jelatyna.user.domain;

import static javax.persistence.GenerationType.*;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Data
@Accessors(chain = true)
public class Registration {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String sex;

    private String city;

    private String size;

    private String experience;

    private String position;

    private LocalDateTime registrationDate;

    private LocalDateTime arrivalDate;

    private LocalDateTime stampDate;

    private LocalDateTime ticketSendDate;

    private boolean drawn;

}
