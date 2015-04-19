package pl.confitura.jelatyna.user.password;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.confitura.jelatyna.user.TokenGenerator;
import pl.confitura.jelatyna.user.TokenInvalidException;
import pl.confitura.jelatyna.user.domain.User;
import pl.confitura.jelatyna.user.UserRepository;

@RestController
@RequestMapping("/api/password")
public class PasswordController {
    private UserRepository repository;

    private TokenGenerator generator;

    @Autowired
    public PasswordController(UserRepository repository, TokenGenerator generator) {
        this.repository = repository;
        this.generator = generator;
    }

    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public void reset(@RequestBody PasswordRequest passwordRequest) {
        User user = repository.findByToken(passwordRequest.getToken()).orElseThrow(TokenInvalidException::new);
        user.setPassword(generator.encrypt(passwordRequest.getValue()));
    }
}
