package pl.confitura.jelatyna.user.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.confitura.jelatyna.email.EmailService;
import pl.confitura.jelatyna.user.TokenGenerator;
import pl.confitura.jelatyna.user.UserRepository;
import pl.confitura.jelatyna.user.domain.Person;
import pl.confitura.jelatyna.user.domain.User;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static pl.confitura.jelatyna.user.domain.Authority.ADMIN;

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

    @RequestMapping(method = POST)
    @Transactional
    public HttpStatus create(@Valid @RequestBody Person person) {
        User user = new User()
            .setPerson(person)
            .addAuthority(ADMIN)
            .token(tokenGenerator.generate());
        repository.save(user);
        sender.adminCreated(user.getPerson());
        return HttpStatus.CREATED;
    }

    @RequestMapping(method = PUT)
    @Transactional
    public void update(@Valid @RequestBody User user) {
        repository.findOne(user.getId())
            .ifPresent(stored -> stored.copyFrom(user));
    }

}
