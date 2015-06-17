package pl.confitura.jelatyna.user.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Email;
import pl.confitura.jelatyna.user.domain.Authority;
import pl.confitura.jelatyna.user.domain.Person;
import pl.confitura.jelatyna.user.domain.User;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class NewUser {

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    @Email
    private String email;
    @NotNull
    private Authority role;


    public User asUser() {
        return new User()
            .addAuthority(role)
            .setPerson(new Person()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email));
    }

}
