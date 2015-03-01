package pl.confitura.jelatyna.admin;

import java.util.List;

@org.springframework.stereotype.Repository
public interface Repository extends org.springframework.data.repository.Repository<Admin, Long> {

    Admin save(Admin admin);

    List<Admin> findAll();
}
