package pl.confitura.jelatyna.user.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import pl.confitura.jelatyna.user.domain.Authority;
import pl.confitura.jelatyna.user.domain.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Accessors(chain = true)
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String bio;
    private String twitter;
    private String code;
    private List<Authority> authorities= new ArrayList<>();

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
        dto.authorities.addAll((Collection<? extends Authority>) user.getAuthorities());
        return dto ;
    }
}
