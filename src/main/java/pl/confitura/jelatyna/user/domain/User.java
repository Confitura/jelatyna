package pl.confitura.jelatyna.user.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Data
@Accessors(chain = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @Valid
    private Person person;

    @ElementCollection
    private List<Role> roles = new ArrayList<>();

    @JsonIgnore
    private String password;

    public User token(String token) {
        this.person.setToken(token);
        return this;
    }

    public User addRole(Role role) {
        roles.add(role);
        return this;
    }

}
