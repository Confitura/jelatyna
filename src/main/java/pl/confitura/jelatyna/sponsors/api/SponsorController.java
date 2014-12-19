package pl.confitura.jelatyna.sponsors.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.confitura.jelatyna.sponsors.SponsorService;
import pl.confitura.jelatyna.sponsors.domain.Sponsor;
import pl.confitura.jelatyna.sponsors.domain.SponsorGroup;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class SponsorController {

    @Autowired
    private SponsorService sponsorService;

    @RequestMapping(value = "/sponsorGroup", method = POST, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public void createSponsorGroup(@RequestBody SponsorGroupDto sponsorGroupDto) {
        sponsorService.createSponsorGroup(new SponsorGroup(sponsorGroupDto.getName()).withLabel(sponsorGroupDto.getLabel()));
    }

    @RequestMapping(value = "/sponsor", method = POST, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public void createSponsorGroup(@RequestBody SponsorDto sponsorDto) {
        sponsorService.createSponsorInGroup(new Sponsor().withName(sponsorDto.getName()).withDescription(sponsorDto.getDescription()), sponsorDto.getSponsorGroupName());
    }
}
