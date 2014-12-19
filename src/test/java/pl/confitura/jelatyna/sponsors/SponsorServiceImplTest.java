package pl.confitura.jelatyna.sponsors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.confitura.jelatyna.sponsors.domain.Sponsor;
import pl.confitura.jelatyna.sponsors.domain.SponsorGroup;
import pl.confitura.jelatyna.sponsors.repository.SponsorGroupRepository;
import pl.confitura.jelatyna.sponsors.repository.SponsorRepository;

import java.util.List;

import static com.google.common.collect.Lists.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SponsorServiceImplTest {

    @Mock
    private SponsorGroupRepository sponsorGroupRepository;
    @Mock
    private SponsorRepository sponsorRepository;
    @InjectMocks
    private SponsorServiceImpl sponsorService = new SponsorServiceImpl();

    @Test
    public void createSponsorGroup() {
        SponsorGroup sponsorGroup = new SponsorGroup("platinum");

        sponsorService.createSponsorGroup(sponsorGroup);

        verify(sponsorGroupRepository).save(sponsorGroup);
    }

    @Test
    public void createSponsorInGroup() {
        Sponsor sponsor = new Sponsor().withName("Computex");
        Sponsor savedSponsor = new Sponsor().withId(5).withName("Computex");
        when(sponsorRepository.save(sponsor)).thenReturn(savedSponsor);
        SponsorGroup platinumGroup = new SponsorGroup("platinum");
        when(sponsorGroupRepository.findByName("platinum")).thenReturn(platinumGroup);

        sponsorService.createSponsorInGroup(sponsor, "platinum");

        assertThat(platinumGroup.getSponsors()).contains(savedSponsor);
    }

    @Test
    public void getSponsorGroups() {
        List<SponsorGroup> storedSponsorGroups = newArrayList(new SponsorGroup("platinum"), new SponsorGroup("gold"));
        when(sponsorGroupRepository.findAll()).thenReturn(storedSponsorGroups);

        List<SponsorGroup> sponsorGroups = sponsorService.getSponsorGroups();

        assertThat(sponsorGroups).isEqualTo(storedSponsorGroups);
    }
}