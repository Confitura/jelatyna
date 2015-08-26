package pl.confitura.jelatyna.user.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import pl.confitura.jelatyna.AbstractEntity;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class User extends AbstractEntity {

    @OneToOne(cascade = CascadeType.ALL)
    @Valid
    private Person person;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Role> roles = new ArrayList<>();

    private String twitter;

    private String code;

    @Lob
    private String bio;

    @JsonIgnore
    private String password;

    @Column(unique = true)
    private String token;

    @Lob
    private byte[] photo;

    public User(User user) {
        this.id = user.id;
        this.roles = user.roles;
        this.twitter = user.twitter;
        this.bio = user.code;
        this.password = user.password;
        this.person = user.person;
    }

    public User token(String token) {
        this.token = token;
        return this;
    }

    public User addRole(Role role) {
        roles.add(role);
        return this;
    }

    public void copyFrom(User user) {
        person.copyFrom(user.getPerson());
        twitter = user.twitter;
        code = user.code;
        bio = user.bio;
    }

    public void resetToken() {
        token = null;
    }
}
