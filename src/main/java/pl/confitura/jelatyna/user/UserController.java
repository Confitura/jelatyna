package pl.confitura.jelatyna.user;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import pl.confitura.jelatyna.email.service.EmailService;
import pl.confitura.jelatyna.user.domain.User;
import pl.confitura.jelatyna.user.dto.NewUser;
import pl.confitura.jelatyna.user.dto.UserDto;

@RestController("userController")
@RequestMapping("/users")
public class UserController {

    private UserRepository repository;

    private TokenGenerator tokenGenerator;

    public EmailService sender;


    @Autowired
    public UserController(UserRepository repository, TokenGenerator tokenGenerator, EmailService sender) {
        this.repository = repository;
        this.tokenGenerator = tokenGenerator;
        this.sender = sender;
    }

    @RequestMapping(value = "/login", method = GET)
    public UserDto user(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return UserDto.copyFrom(user);
    }

    @RequestMapping(method = POST)
//    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<Void> create(@Valid @RequestBody NewUser newUser) {
        User user = newUser.asUser().token(tokenGenerator.generate());
        user = repository.save(user);
        sender.created(user, newUser.getRole());
        return ResponseEntity.created(URI.create("/users/"+user.getId())).build();
    }

    @RequestMapping(method = GET)
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDto> allUsers() {
        return repository.findAll().stream()
                .map(UserDto::copyFrom)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/{id}")
    public UserDto aUser(@PathVariable String id) {
        return repository.findOne(id).map(UserDto::copyFrom)
                .orElseThrow(NoSuchElementException::new);
    }

    @RequestMapping(method = PATCH)
    @Transactional
    public void update(@Valid @RequestBody UserDto user) {
        repository.findOne(user.getId())
                .ifPresent(user::copyTo);
    }

    @RequestMapping(value = "/{id}/photo", method = POST)
    @Transactional
    public void uploadPhoto(final MultipartFile file, @PathVariable String id) throws IOException {
        repository.findOne(id).get().setPhoto(file.getBytes());
    }

    @RequestMapping(value = "/{id}/photo", method = GET, produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getPhoto(@PathVariable String id) throws IOException {
        User user = repository.findOne(id).get();
        return ResponseEntity.ok(user.getPhoto());
    }

}
