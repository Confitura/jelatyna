package pl.confitura.jelatyna.presentation;

import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

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
@ToString(exclude = "owner")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "owner")
@Accessors(chain = true)
public class Presentation extends AbstractEntity {
    private String title;

    private String shortDescription;

    private String description;

    private PresentationLevel level;

    @ElementCollection
    private Set<String> tags;

    @JsonIgnore
    @ManyToOne(optional = false)
    private User owner;

    public Presentation setOwner(User owner) {
        this.owner = owner;
        owner.addPresentation(this);
        return this;
    }
}
