package pl.confitura.jelatyna;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.experimental.Accessors;

@MappedSuperclass
@Data
@Accessors(chain = true)
public class AbstractEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    protected String id;

}
