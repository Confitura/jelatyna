package pl.confitura.jelatyna.user.dto;

import java.util.ArrayList;
import java.util.List;

import groovy.transform.EqualsAndHashCode;
import lombok.Data;
import lombok.experimental.Accessors;
import pl.confitura.jelatyna.user.domain.Role;
import pl.confitura.jelatyna.user.domain.User;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class UserDto extends PersonDto {

    private String id;
    private String bio;
    private String twitter;
    private String code;
    private List<Role> roles = new ArrayList<>();

    public void copyTo(User user) {
        user.setBio(bio)
            .setCode(code)
            .setTwitter(twitter)
            .getPerson()
            .setFirstName(firstName)
            .setLastName(lastName)
            .setEmail(email);
    }

    public static UserDto copyFrom(User user) {
        UserDto dto = new UserDto();
        dto.id = user.getId();
        dto.firstName = user.getPerson().getFirstName();
        dto.lastName = user.getPerson().getLastName();
        dto.email = user.getPerson().getEmail();
        dto.bio = user.getBio();
        dto.twitter = user.getTwitter();
        dto.code = user.getCode();
        dto.roles.addAll(user.getRoles());
        return dto ;
    }
}
