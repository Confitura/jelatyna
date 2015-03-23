package pl.confitura.jelatyna.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController("adminController")
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @RequestMapping(method = GET)
    public List<Admin> all() {
        return adminService.findAll();
    }

    @RequestMapping(value = "/create/{token}")
    public Admin getBy(@PathVariable String token) {
        return adminService.findByToken(token);
    }

    @RequestMapping(method = POST)
    public ResponseEntity<Long> create(@Valid @RequestBody Admin admin) {
        Admin saved = adminService.create(admin);
        return new ResponseEntity<Long>(saved.getId(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}",method = PUT)
    public void update(@PathVariable("id") Long id, @Valid @RequestBody Admin admin) {
        adminService.update(id, admin);
    }
}
