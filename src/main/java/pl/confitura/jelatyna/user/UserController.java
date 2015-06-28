package pl.confitura.jelatyna.user;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.confitura.jelatyna.email.service.EmailService;
import pl.confitura.jelatyna.user.domain.User;
import pl.confitura.jelatyna.user.dto.NewUser;
import pl.confitura.jelatyna.user.dto.UserDto;

@RestController("userController")
@RequestMapping("/api/user")
public class UserController {

    private UserRepository repository;

    private TokenGenerator tokenGenerator;

    private EmailService sender;

    @Autowired
    public UserController(UserRepository repository, TokenGenerator tokenGenerator, EmailService sender) {
        this.repository = repository;
        this.tokenGenerator = tokenGenerator;
        this.sender = sender;
    }

    @RequestMapping(value = "/login")
    public UserDto user(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return UserDto.copyFrom(user);
    }

    @RequestMapping(method = POST)
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public HttpStatus create(@Valid @RequestBody NewUser newUser) {
        User user = newUser.asUser().token(tokenGenerator.generate());
        repository.save(user);
        sender.adminCreated(user.getPerson());
        return HttpStatus.CREATED;
    }

    @RequestMapping(method = PUT)
    @Transactional
    public void update(@Valid @RequestBody UserDto user) {
        repository.findOne(user.getId())
            .ifPresent(user::copyTo);
    }

}
