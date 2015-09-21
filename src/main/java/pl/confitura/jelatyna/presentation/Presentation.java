package pl.confitura.jelatyna.presentation;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import pl.confitura.jelatyna.AbstractEntity;
import pl.confitura.jelatyna.user.domain.User;

@Entity
@Data
@ToString(exclude = "speakers")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "speakers")
@Accessors(chain = true)
public class Presentation extends AbstractEntity {
    private String title;

    private String shortDescription;

    private String description;

    private PresentationLevel level;

    @ElementCollection
    private Set<String> tags;

    @JsonIgnore
    @ManyToMany
    private Set<User> speakers = new HashSet<>();

    public Presentation addSpeaker(User speaker) {
        speakers.add(speaker);
        return this;
    }
}
