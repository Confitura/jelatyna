package pl.confitura.jelatyna.sponsors.repository;

import pl.confitura.jelatyna.sponsors.domain.SponsorGroup;

public interface SponsorGroupRepository {
    void save(SponsorGroup sponsorGroup);

    SponsorGroup findByName(String name);
}
