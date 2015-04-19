package pl.confitura.jelatyna.user.admin;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import static pl.confitura.jelatyna.user.domain.Role.*;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.confitura.jelatyna.email.EmailService;
import pl.confitura.jelatyna.user.domain.Person;
import pl.confitura.jelatyna.user.TokenGenerator;
import pl.confitura.jelatyna.user.TokenInvalidException;
import pl.confitura.jelatyna.user.domain.User;
import pl.confitura.jelatyna.user.UserRepository;

@RestController("adminController")
@RequestMapping("/api/admin")
public class AdminController {

    private UserRepository repository;

    private TokenGenerator tokenGenerator;

    private EmailService sender;

    @Autowired
    public AdminController(UserRepository repository, TokenGenerator tokenGenerator, EmailService sender) {
        this.repository = repository;
        this.tokenGenerator = tokenGenerator;
        this.sender = sender;
    }

    @RequestMapping
    public List<User> all() {
        return repository.findAll();
    }

    @RequestMapping(value = "/password/{token}")
    public User getBy(@PathVariable String token) {
        return repository.findByToken(token).orElseThrow(TokenInvalidException::new);
    }

    @RequestMapping(method = POST)
    public HttpStatus create(@Valid @RequestBody Person person) {
        User user = new User()
                .setPerson(person)
                .addRole(ADMIN)
                .token(tokenGenerator.generate());
        repository.save(user);
        sender.adminCreated(user.getPerson());
        return HttpStatus.CREATED;
    }

    @RequestMapping(method = PUT)
    public void update(@Valid @RequestBody User user) {
        repository.save(user);
    }
}
