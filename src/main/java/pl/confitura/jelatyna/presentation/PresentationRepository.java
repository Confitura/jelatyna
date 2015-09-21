package pl.confitura.jelatyna.presentation;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.Repository;

public interface PresentationRepository extends Repository<Presentation, String> {

    Presentation save(Presentation presentation);

    Set<Presentation> findAll();

    Optional<Presentation> findOne(String id);
}
