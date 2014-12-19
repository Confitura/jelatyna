package pl.confitura.jelatyna.sponsors.repository.mem;

import org.springframework.stereotype.Component;
import pl.confitura.jelatyna.sponsors.domain.Sponsor;
import pl.confitura.jelatyna.sponsors.repository.SponsorRepository;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SponsorRepositoryImpl extends InMemoryRepository<Sponsor, Integer> implements SponsorRepository {
    private AtomicInteger idSeq = new AtomicInteger(1);

    @Override
    public Sponsor save(Sponsor sponsor) {
        Sponsor savedSponsor = new Sponsor()
                .withId(idSeq.getAndIncrement())
                .withName(sponsor.getName())
                .withDescription(sponsor.getDescription());
        repo.put(savedSponsor.getId(), savedSponsor);
        return savedSponsor;
    }
}
