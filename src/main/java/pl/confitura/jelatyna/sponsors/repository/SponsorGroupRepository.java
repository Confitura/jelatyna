package pl.confitura.jelatyna.sponsors.repository;

import pl.confitura.jelatyna.sponsors.domain.SponsorGroup;

import java.util.List;

public interface SponsorGroupRepository {
    void save(SponsorGroup sponsorGroup);

    SponsorGroup findByName(String name);

    List<SponsorGroup> findAll();
}
