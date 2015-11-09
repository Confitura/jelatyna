package pl.confitura.jelatyna.news;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.experimental.Accessors;
import pl.confitura.jelatyna.user.domain.User;

@Entity
@Data
@Accessors(chain = true)
public class News {
    @Id
    private String id;

    private String title;

    @ManyToOne
    private User author;

    private LocalDateTime publicationDate;

    private boolean published;

    private String shortText;

    private String longText;

}
