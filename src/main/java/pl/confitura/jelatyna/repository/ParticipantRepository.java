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

    @PostAuthorize("(returnObject!=null and returnObject.getEmail() == principal.username) or hasRole('ADMIN')")
    @Override
    Participant findOne(Long aLong);

    @Override
    @PreAuthorize("#participant==null or #participant.getId() == null ")
    Participant save(Participant participant);


    @PreAuthorize("hasRole('ADMIN')")
    @Override
    Iterable<Participant> findAll(Iterable<Long> iterable);

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    Iterable<Participant> findAll();
}
