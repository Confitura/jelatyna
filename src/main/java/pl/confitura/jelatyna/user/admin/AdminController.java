package pl.confitura.jelatyna.user.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.confitura.jelatyna.user.UserRepository;
import pl.confitura.jelatyna.user.domain.Person;
import pl.confitura.jelatyna.user.domain.Role;
import pl.confitura.jelatyna.user.domain.User;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController("adminController")
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository repository;
    private final AdminService adminService;

    @Autowired
    public AdminController(UserRepository repository, AdminService adminService) {
        this.repository = repository;
        this.adminService = adminService;
    }

    @RequestMapping
    public List<User> all() {
        return repository.findAll();
    }

    @Secured(Role.ROLE_ADMIN)
    @RequestMapping(method = POST)
    public HttpStatus create(@Valid @RequestBody Person person) {
        adminService.create(person);
        return HttpStatus.CREATED;
    }

}
