package pl.confitura.jelatyna.participation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.confitura.jelatyna.user.PersonRepository;
import pl.confitura.jelatyna.user.domain.Person;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/participation")
public class ParticipationController {

    private PersonRepository repository;

    @Autowired
    public ParticipationController(PersonRepository repository) {
        this.repository = repository;
    }

    @RequestMapping("/{token}")
    public Person getFor(@PathVariable String token) {
        return getPersonBy(token);
    }

    @RequestMapping(value = "/{token}/confirm", method = RequestMethod.POST)
    @Transactional
    public HttpStatus confirm(@PathVariable String token) {
        getPersonBy(token).arrived();
        return HttpStatus.OK;
    }


    @RequestMapping(value = "/{token}/reject", method = RequestMethod.POST)
    @Transactional
    public HttpStatus reject(@PathVariable String token) {
        getPersonBy(token).reject();
        return HttpStatus.OK;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private void handleNotFoundException(NoSuchElementException ex) {
    }

    private Person getPersonBy(@PathVariable String token) {
        return repository.findByToken(token).get();
    }


}
