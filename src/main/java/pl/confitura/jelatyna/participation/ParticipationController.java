package pl.confitura.jelatyna.participation;

import java.util.List;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.confitura.jelatyna.user.PersonRepository;
import pl.confitura.jelatyna.user.domain.Person;

@RestController
@RequestMapping("/api/participation")
@PreAuthorize("hasAnyRole('ADMIN, VOLUNTEER')")
public class ParticipationController {

    private PersonRepository repository;

    @Autowired
    public ParticipationController(PersonRepository repository) {
        this.repository = repository;
    }

    @RequestMapping("/{text:.+}")
    public List<Person> findAll(@PathVariable String text) {
        return repository.find(text);
    }

    @RequestMapping("/token/{token}")
    public Person getFor(@PathVariable String token) {
        return getPersonBy(token);
    }

    @Transactional
    @RequestMapping(value = "/token/confirm/{token}", method = RequestMethod.POST)
    public void confirm(@PathVariable String token) {
        getPersonBy(token).arrived();
    }

    @Transactional
    @RequestMapping(value = "/token/reject/{token}", method = RequestMethod.POST)
    public void reject(@PathVariable String token) {
        getPersonBy(token).reject();
    }

    @Transactional
    @RequestMapping(value = "/token/stamp/{token}", method = RequestMethod.POST)
    public void stamp(@PathVariable String token) {
        getPersonBy(token).stamp();
    }

    @Transactional
    @RequestMapping(value = "/token/unstamp/{token}", method = RequestMethod.POST)
    public void unstamp(@PathVariable String token) {
        getPersonBy(token).unstamp();
    }

    private Person getPersonBy(@PathVariable String token) {
        return repository.findByToken(token).get();
    }

}
