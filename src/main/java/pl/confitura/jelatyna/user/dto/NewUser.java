package pl.confitura.jelatyna.user.dto;

import groovy.transform.EqualsAndHashCode;
import lombok.Data;
import lombok.experimental.Accessors;
import pl.confitura.jelatyna.user.domain.Role;
import pl.confitura.jelatyna.user.domain.User;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class NewUser extends PersonDto {

    private Role role;

    public User asUser() {
        return new User()
            .addRole(role)
            .setPerson(asPerson());
    }

}
