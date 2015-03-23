package pl.confitura.jelatyna.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("adminRepository")
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByToken(String token);
}
