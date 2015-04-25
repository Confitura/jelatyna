package pl.confitura.jelatyna.infrastructure.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.confitura.jelatyna.user.UserRepository;
import pl.confitura.jelatyna.user.admin.AdminService;
import pl.confitura.jelatyna.user.domain.Person;
import pl.confitura.jelatyna.user.domain.User;
import pl.confitura.jelatyna.user.password.PasswordController;
import pl.confitura.jelatyna.user.password.PasswordRequest;

@Slf4j
@Component
public class CreatingDefaultAdminBean implements InitializingBean {

	public static final String DEFAULT_ADMIN_EMAIL = "admin@admin.pl";
	public static final String DEFAULT_ADMIN_PASSWORD = "admin";

	private final AdminService adminController;
	private final UserRepository userRepository;
	private final PasswordController passwordController;

	@Autowired
	public CreatingDefaultAdminBean(AdminService adminController, UserRepository userRepository, PasswordController passwordController) {
		this.adminController = adminController;
		this.userRepository = userRepository;
		this.passwordController = passwordController;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		if (thereIsAlreadyAnAdmin()) {
			return;
		} else {
			createPerson(DEFAULT_ADMIN_EMAIL);
			String token = getToken(DEFAULT_ADMIN_EMAIL);
			changePassword(DEFAULT_ADMIN_PASSWORD, token);

			log.info("created default admin with email '{}' and password '{}'", DEFAULT_ADMIN_EMAIL, DEFAULT_ADMIN_PASSWORD);
		}
	}

	private boolean thereIsAlreadyAnAdmin() {
		return userRepository.count() > 0;
	}

	private String getToken(String email) {
		User user = userRepository.findByEmail(email).get();
		return user.getPerson().getToken();
	}

	private void changePassword(String password, String token) {
		PasswordRequest passwordRequest = new PasswordRequest();
		passwordRequest.setToken(token);
		passwordRequest.setValue(password);
		passwordController.reset(passwordRequest);
	}

	private void createPerson(String email) {
		Person person = new Person();
		person.setEmail(email);
		person.setFirstName("admin");
		person.setLastName("admin");
		adminController.create(person);
	}
}
