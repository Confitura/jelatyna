package pl.confitura.jelatyna.user.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.confitura.jelatyna.email.EmailService;
import pl.confitura.jelatyna.user.TokenGenerator;
import pl.confitura.jelatyna.user.UserRepository;
import pl.confitura.jelatyna.user.domain.Person;
import pl.confitura.jelatyna.user.domain.User;

import static pl.confitura.jelatyna.user.domain.Role.ADMIN;

@Service
public class AdminService {

	private final UserRepository repository;
	private final TokenGenerator tokenGenerator;
	private final EmailService sender;

	@Autowired
	public AdminService(UserRepository repository, TokenGenerator tokenGenerator, EmailService sender) {
		this.repository = repository;
		this.tokenGenerator = tokenGenerator;
		this.sender = sender;
	}

	public void create(Person person) {
		User user = new User()
				.setPerson(person)
				.addRole(ADMIN)
				.token(tokenGenerator.generate());
		repository.save(user);
		sender.adminCreated(user.getPerson());
	}
}
