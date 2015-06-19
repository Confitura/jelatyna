package pl.confitura.jelatyna.user.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import pl.confitura.jelatyna.user.domain.Person;

@Data
@Accessors(chain = true)
public class PersonDto {

    @NotEmpty
    String firstName;
    @NotEmpty
    String lastName;
    @NotEmpty
    @Email
    String email;

    public Person asPerson() {
        return new Person()
            .setFirstName(firstName)
            .setLastName(lastName)
            .setEmail(email);
    }
}
