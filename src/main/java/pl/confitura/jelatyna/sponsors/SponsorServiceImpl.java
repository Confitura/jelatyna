package pl.confitura.jelatyna.sponsors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.confitura.jelatyna.sponsors.domain.Sponsor;
import pl.confitura.jelatyna.sponsors.domain.SponsorGroup;
import pl.confitura.jelatyna.sponsors.repository.SponsorGroupRepository;
import pl.confitura.jelatyna.sponsors.repository.SponsorRepository;

@Service
@Transactional
public class SponsorServiceImpl implements SponsorService {

    @Autowired
    private SponsorRepository sponsorRepository;
    @Autowired
    private SponsorGroupRepository sponsorGroupRepository;

    @Override
    public void createSponsorGroup(SponsorGroup sponsorGroup) {
        sponsorGroupRepository.save(sponsorGroup);
    }

    @Override
    public void createSponsorInGroup(Sponsor sponsor, String sponsorGroupName) {
        SponsorGroup sponsorGroup = sponsorGroupRepository.findByName(sponsorGroupName);
        Sponsor savedSponsor = sponsorRepository.save(sponsor);
        sponsorGroup.addSponsor(savedSponsor);
    }

}
