package pl.confitura.jelatyna.participant;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

}
