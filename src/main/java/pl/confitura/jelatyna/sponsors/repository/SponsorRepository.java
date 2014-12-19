package pl.confitura.jelatyna.sponsors.repository;

import pl.confitura.jelatyna.sponsors.domain.Sponsor;

public interface SponsorRepository {
    Sponsor save(Sponsor sponsor);
}
