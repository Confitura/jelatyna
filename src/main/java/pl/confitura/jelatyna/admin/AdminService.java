package pl.confitura.jelatyna.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.confitura.jelatyna.email.EmailService;

import java.util.List;

@Service
public class AdminService {
    private final AdminRepository repository;
    private final TokenGenerator tokenGenerator;
    private final EmailService sender;

    @Autowired
    public AdminService(AdminRepository repository, TokenGenerator tokenGenerator, EmailService sender) {
        this.repository = repository;
        this.tokenGenerator = tokenGenerator;
        this.sender = sender;
    }

    public List<Admin> findAll() {
        return repository.findAll();
    }

    public Admin findByToken(String token) {
        return repository.findByToken(token).orElseThrow(AdminNotExists::new);
    }

    public Admin create(Admin admin) {
        admin.setToken(tokenGenerator.generate());
        Admin saved = repository.save(admin);
        sender.notifyAdminAboutCreatedAccount(saved);
        return saved;
    }

    public void update(Long id, Admin admin) {
        admin.setId(id);
        repository.save(admin);
    }
}
