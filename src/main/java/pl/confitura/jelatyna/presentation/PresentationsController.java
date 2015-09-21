package pl.confitura.jelatyna.presentation;

import static java.util.stream.Collectors.*;

import java.net.URI;
import java.util.Set;
import java.util.function.Predicate;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.confitura.jelatyna.user.UserRepository;
import pl.confitura.jelatyna.user.domain.User;

@RestController
public class PresentationsController {

    private PresentationRepository repository;

    private UserRepository userRepository;

    @Autowired
    public PresentationsController(PresentationRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Transactional
    @RequestMapping(value = "/users/{userId}/presentations", method = RequestMethod.POST)
    public ResponseEntity<Void> save(@PathVariable String userId, @RequestBody Presentation presentation) {
        User user = userRepository.findOne(userId).get();
        Presentation saved = repository.save(presentation.setOwner(user));
        saved.setOwner(user);
        return ResponseEntity.created(URI.create("/users/" + userId + "/presentations/" + saved.getId())).build();
    }

    @RequestMapping(value = "/users/{userId}/presentations", method = RequestMethod.GET)
    public Set<Presentation> getPresentationsFor(@PathVariable String userId) {
        User user = userRepository.findOne(userId).get();
        return user.getPresentations();
    }

    @RequestMapping(value = "/presentations", method = RequestMethod.GET)
    public Set<Presentation> getAll(@RequestParam(value = "tags", required = false) Set<String> tags,
                                    @RequestParam(value = "level", required = false) PresentationLevel level) {
        return repository.findAll().stream()
                .filter(by(tags))
                .filter(by(level))
                .collect(toSet());
    }

    private Predicate<Presentation> by(PresentationLevel level) {
        return presentation -> level == null || presentation.getLevel() == level;
    }

    private Predicate<Presentation> by(Set<String> tags) {
        return presentation -> tags == null || presentation.getTags().containsAll(tags);
    }
}
