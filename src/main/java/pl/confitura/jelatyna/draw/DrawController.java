package pl.confitura.jelatyna.draw;

import java.util.Collections;
import java.util.List;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.confitura.jelatyna.user.PersonRepository;
import pl.confitura.jelatyna.user.domain.Person;

@RestController
@RequestMapping("/draw")
@PreAuthorize("hasRole('ADMIN')")
public class DrawController {
    @Autowired
    private PersonRepository repository;

    @Transactional
    @RequestMapping(value = "/next", method = RequestMethod.POST)
    public Person draw() {
        List<Person> people = repository.findNotDrawnWithStamps();
        Collections.shuffle(people);
        Person person = people.get(0);
        person.drown();
        return person;
    }

    @Transactional
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public void resetDrawing() {
        repository.resetDrawing();
    }

}
