package pl.confitura.jelatyna.participation;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.confitura.jelatyna.user.PersonRepository;
import pl.confitura.jelatyna.user.domain.Person;

@RestController
@RequestMapping("/participation")
@PreAuthorize("hasAnyRole('ADMIN, VOLUNTEER')")
public class ParticipationController {

    private PersonRepository repository;

    @Autowired
    public ParticipationController(PersonRepository repository) {
        this.repository = repository;
    }

    @RequestMapping
    public List<Person> findAll(@RequestParam("query") String query) {
        return repository.find(query);
    }

    @RequestMapping("/{id}")
    public Person getBy(@PathVariable String id) {
        return getPersonBy(id);
    }

    @Transactional
    @RequestMapping(value = "/{id}/confirm", method = POST)
    public void confirm(@PathVariable String id) {
//        getPersonBy(id).arrived();
    }

    @Transactional
    @RequestMapping(value = "/{id}/reject", method = POST)
    public void reject(@PathVariable String id) {
//        getPersonBy(id).reject();
    }

    @Transactional
    @RequestMapping(value = "/{id}/stamp", method = POST)
    public void stamp(@PathVariable String id) {
//        getPersonBy(id).stamp();
    }

    @Transactional
    @RequestMapping(value = "/{id}/unstamp", method = POST)
    public void unstamp(@PathVariable String id) {
//        getPersonBy(id).unstamp();
    }

    private Person getPersonBy(String id) {
        return repository.findOne(id).get();
    }

}
