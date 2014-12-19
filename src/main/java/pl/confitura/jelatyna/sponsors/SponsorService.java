package pl.confitura.jelatyna.sponsors;

import pl.confitura.jelatyna.sponsors.domain.Sponsor;
import pl.confitura.jelatyna.sponsors.domain.SponsorGroup;

public interface SponsorService {
    void createSponsorGroup(SponsorGroup sponsorGroup);

    void createSponsorInGroup(Sponsor sponsor, String sponsorGroupName);
}
