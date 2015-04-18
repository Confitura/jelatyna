package pl.confitura.jelatyna.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.confitura.jelatyna.common.TokenGenerator;
import pl.confitura.jelatyna.email.EmailService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static pl.confitura.jelatyna.admin.Role.ADMIN;

@RestController("adminController")
@RequestMapping("/api/admin")
public class Controller {

    private Repository repository;
    private TokenGenerator tokenGenerator;
    private EmailService sender;

    @Autowired
    public Controller(Repository repository, TokenGenerator tokenGenerator, EmailService sender) {
        this.repository = repository;
        this.tokenGenerator = tokenGenerator;
        this.sender = sender;
    }

    @RequestMapping
    public List<User> all() {
        return repository.findAll();
    }

    @RequestMapping(value = "/create/{token}")

    public User getBy(@PathVariable String token) {
        return repository.findByToken(token).orElseThrow(TokenInvalidException::new);
    }

    @RequestMapping(method = POST)
    public HttpStatus create(@Valid @RequestBody User user) {
        user.addRole(ADMIN);
        user.token(tokenGenerator.generate());
        repository.save(user);
        sender.adminCreated(user.getPerson());
        return HttpStatus.CREATED;
    }

    @RequestMapping(method = PUT)
    public void update(@Valid @RequestBody User user) {
        repository.save(user);
    }
}
