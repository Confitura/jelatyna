package pl.confitura.jelatyna.sponsors.repository.mem;

import org.springframework.stereotype.Component;
import pl.confitura.jelatyna.sponsors.domain.SponsorGroup;
import pl.confitura.jelatyna.sponsors.repository.SponsorGroupRepository;

import java.util.List;

import static com.google.common.collect.Lists.*;

@Component
public class SponsorGroupRepositoryImpl extends InMemoryRepository<SponsorGroup, String> implements SponsorGroupRepository {

    @Override
    public void save(SponsorGroup sponsorGroup) {
        repo.put(sponsorGroup.getName(), sponsorGroup);
    }

    @Override
    public SponsorGroup findByName(String name) {
        return repo.get(name);
    }

    @Override
    public List<SponsorGroup> findAll() {
        return newArrayList(repo.values());
    }
}
