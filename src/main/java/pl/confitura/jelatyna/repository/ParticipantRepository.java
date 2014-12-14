package pl.confitura.jelatyna.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import pl.confitura.jelatyna.domain.Participant;

@Repository
@RepositoryRestResource
public interface ParticipantRepository extends CrudRepository<Participant, Long> {
}
