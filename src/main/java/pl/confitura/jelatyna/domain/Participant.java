package pl.confitura.jelatyna.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode
@ToString
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

    @OneToOne(mappedBy = "participant")
    private User user;

}
