package pl.confitura.jelatyna.user.domain;

import static java.time.LocalDateTime.*;
import static java.util.Optional.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import pl.confitura.jelatyna.AbstractEntity;

@Entity
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Person extends AbstractEntity {

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @Email
    @NotEmpty
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    private Registration registration;

    public void copyFrom(Person person) {
        email = person.email;
        firstName = person.firstName;
        lastName = person.lastName;
    }

    public void arrived() {
        registration.setArrivalDate(now());
    }

    public boolean isArrived() {
        return ofNullable(registration)
                .orElse(new Registration()).getArrivalDate() != null;
    }

    public boolean isRegistered() {
        return ofNullable(registration)
                .orElse(new Registration()).getRegistrationDate() != null;
    }

    public void reject() {
        registration.setArrivalDate(null);
    }

    public void stamp() {
        registration.setStampDate(now());
    }

    public void unstamp() {
        registration.setStampDate(null);
    }

    public boolean isStamped() {
        return registration.getStampDate() != null;
    }

    public void drown() {
        registration.setDrawn(true);
    }

    public void ticketSent() {
        registration.setTicketSendDate(now());
    }

    public boolean ticketNotSentYet() {
        return registration.getTicketSendDate() == null;
    }
}
