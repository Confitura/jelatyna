package pl.confitura.jelatyna.sponsors;

import org.springframework.stereotype.Component;
import pl.confitura.jelatyna.sponsors.domain.SponsorGroup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SponsorGroupRepositoryImpl implements SponsorGroupRepository {

    private Map<String, SponsorGroup> repo = new ConcurrentHashMap<>();

    @Override
    public void save(SponsorGroup sponsorGroup) {
        repo.put(sponsorGroup.getName(), sponsorGroup);
    }
}
