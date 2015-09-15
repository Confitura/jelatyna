package pl.confitura.jelatyna.sponsor;

import javax.persistence.Entity;
import javax.persistence.Lob;

import groovy.transform.EqualsAndHashCode;
import lombok.Data;
import lombok.experimental.Accessors;
import pl.confitura.jelatyna.AbstractEntity;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Sponsor extends AbstractEntity{

    private String name;

    private String url;

    private String info;

    private String type;

    @Lob
    private byte[] logo;
}
