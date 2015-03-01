package pl.confitura.jelatyna.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.confitura.jelatyna.common.TokenGenerator;
import pl.confitura.jelatyna.email.EmailService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

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
    public List<Admin> all() {
        return repository.findAll();
    }

    @RequestMapping(method = POST)
    public void create(@Valid @RequestBody Admin admin) {
        admin.token(tokenGenerator.generate());
        repository.save(admin);
        sender.adminCreated(admin);
    }

    @RequestMapping(method = PUT)
    public void update(@Valid @RequestBody Admin admin) {
        repository.save(admin);
    }
}
