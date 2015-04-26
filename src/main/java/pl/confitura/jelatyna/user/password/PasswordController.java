package pl.confitura.jelatyna.user.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.confitura.jelatyna.user.TokenInvalidException;
import pl.confitura.jelatyna.user.UserRepository;
import pl.confitura.jelatyna.user.domain.User;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/api/password")
public class PasswordController {

    private UserRepository repository;

    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    public PasswordController(UserRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public void reset(@RequestBody PasswordRequest passwordRequest) {
        User user = repository.findByToken(passwordRequest.getToken()).orElseThrow(TokenInvalidException::new);
        user.setPassword(encoder.encode(passwordRequest.getValue()));
    }
}
