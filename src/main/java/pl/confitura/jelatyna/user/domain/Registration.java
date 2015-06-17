package pl.confitura.jelatyna.user.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.time.LocalDateTime;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
@Accessors(chain = true)
public class Registration {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private String size = "L";
    private String experience;
    private String position;
    private LocalDateTime arrivalDate;

}
