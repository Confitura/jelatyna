package pl.confitura.jelatyna.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import pl.confitura.jelatyna.domain.Participant;

@Repository
@RepositoryRestResource
public interface ParticipantRepository extends CrudRepository<Participant, Long> {

    @PostAuthorize("returnObject.email == principal.username or hasRole('ROLE_ADMIN')")
    @Override
    Participant findOne(Long aLong);

    @Override
    @PreAuthorize("#participant.id == null ")
    Participant save(Participant participant);
}
