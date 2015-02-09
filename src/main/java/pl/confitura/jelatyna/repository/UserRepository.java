package pl.confitura.jelatyna.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Repository;
import pl.confitura.jelatyna.domain.User;

import java.util.Optional;

@Repository
@RepositoryRestResource
public interface UserRepository extends CrudRepository<User, Long> {
//    @PostAuthorize("returnObject.participant.email == principal.username or hasRole('ROLE_ADMIN')")
    Optional<User> findByParticipantEmail(String email);

    @Override
    @PostAuthorize("(returnObject!=null and returnObject.getUsername() == principal.getUsername()) or hasRole('ROLE_ADMIN')")
    User findOne(Long id);

}
