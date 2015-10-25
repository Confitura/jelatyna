package pl.confitura.jelatyna.sponsor;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/sponsors")
public class SponsorController {

    private SponsorRepository repository;

    @Autowired
    public SponsorController(SponsorRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<Void> create(@RequestBody Sponsor sponsor) throws Exception {
        sponsor = repository.save(sponsor);
        return ResponseEntity.created(URI.create("/sponsors/" + sponsor.getId())).build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Sponsor> getAll(@RequestParam(defaultValue = "") String type) {
        if (type.isEmpty()) {
            return repository.findAll();
        }
        return repository.findByType(type);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Sponsor getById(@PathVariable String id) {
        return repository.findOne(id).get();
    }

    @RequestMapping(value = "/{id}/logo", method = POST)
    @Transactional
    public void uploadLogo(MultipartFile file, @PathVariable String id) throws IOException {
        repository.findOne(id).get().setLogo(file.getBytes());
    }

    @RequestMapping(value = "/{id}/logo", method = GET, produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getLogo(@PathVariable String id) throws IOException {
        Sponsor sponsor = repository.findOne(id).get();
        return ResponseEntity.ok(sponsor.getLogo());
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    public ResponseEntity<Void> delete(@PathVariable String id) {
        repository.delete(id);
        return ResponseEntity.ok().build();
    }

}
