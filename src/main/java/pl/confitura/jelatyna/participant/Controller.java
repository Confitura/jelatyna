package pl.confitura.jelatyna.participant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController("participantController")
@RequestMapping("/api/participant")
public class Controller {

    @Autowired
    private Repository repository;
    
    @RequestMapping(method = POST)
    public void create(@Valid @RequestBody Participant participant) {
        repository.save(participant);
    }
    
    @RequestMapping(method = PUT)
    public void update(@Valid @RequestBody Participant participant) {
        repository.save(participant);
    }
    
    @RequestMapping(method = GET)
    public List<Participant> get(){
        return repository.findAll();
    }
    
}
