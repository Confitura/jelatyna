package pl.confitura.jelatyna.user.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import pl.confitura.jelatyna.user.domain.Authority;
import pl.confitura.jelatyna.user.domain.User;

@Data
@Accessors(chain = true)
public class NewUser extends PersonDto {

    private Authority role;

    public User asUser() {
        return new User()
            .addAuthority(role)
            .setPerson(asPerson());
    }

}
