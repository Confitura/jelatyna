package pl.confitura.jelatyna.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

}
