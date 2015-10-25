package pl.confitura.jelatyna.sponsor;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

public interface SponsorRepository extends Repository<Sponsor, String> {
    Sponsor save(Sponsor sponsor);

    List<Sponsor> findAll();

    Optional<Sponsor> findOne(String id);

    List<Sponsor> findByType(String type);

    void delete(String id);

}
