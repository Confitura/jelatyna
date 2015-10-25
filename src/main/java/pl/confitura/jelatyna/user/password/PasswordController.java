package pl.confitura.jelatyna.user.password;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.confitura.jelatyna.email.service.EmailService;
import pl.confitura.jelatyna.user.TokenGenerator;
import pl.confitura.jelatyna.user.TokenInvalidException;
import pl.confitura.jelatyna.user.UserRepository;
import pl.confitura.jelatyna.user.dto.UserDto;

@RestController
@RequestMapping("/users/")
public class PasswordController {

    private UserRepository repository;

    private TokenGenerator generator;

    private EmailService emailService;

    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    public PasswordController(UserRepository repository, TokenGenerator generator, EmailService emailService) {
        this.repository = repository;
        this.generator = generator;
        this.emailService = emailService;
    }


    @RequestMapping(value = "/{id}/password-reset/{token}", method = POST)
    @Transactional
    public void doReset(@PathVariable("id") String id, @PathVariable("token") String token,
                        @RequestBody @Valid PasswordRequest passwordRequest) {
        repository.findOne(id)
                .filter(user -> token.equals(user.getToken()))
                .orElseThrow(TokenInvalidException::new)
                .setPassword(encoder.encode(passwordRequest.getValue()))
                .resetToken();
    }

    @RequestMapping(value = "/password-reset", method = POST)
    @Transactional
    public void requestReset(@RequestBody UserDto userDto) {
        repository
                .findByEmail(userDto.getEmail())
                .ifPresent(user -> {
                            user.setToken(generator.generate());
                            emailService.passwordResetRequested(user);
                        }
                );
    }
}
