package pl.confitura.jelatyna.user.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.confitura.jelatyna.email.service.EmailService;
import pl.confitura.jelatyna.user.TokenGenerator;
import pl.confitura.jelatyna.user.TokenInvalidException;
import pl.confitura.jelatyna.user.UserRepository;
import pl.confitura.jelatyna.user.domain.User;
import pl.confitura.jelatyna.user.dto.UserDto;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/password")
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

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @Transactional
    public void reset(@RequestBody @Valid PasswordRequest passwordRequest) {
        User user = repository.findByToken(passwordRequest.getToken())
            .orElseThrow(TokenInvalidException::new);
        user.setPassword(encoder.encode(passwordRequest.getValue()));
        user.resetToken();
    }

    @RequestMapping(value = "/request", method = RequestMethod.POST)
    @Transactional
    public void requestReset(@RequestBody UserDto userDto) {
        User user = repository.findByEmail(userDto.getEmail()).get();
        user.getPerson().setToken(generator.generate());
        emailService.passwordResetRequested(user);
    }
}
