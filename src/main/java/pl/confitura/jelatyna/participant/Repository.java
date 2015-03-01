package pl.confitura.jelatyna.participant;

import java.util.List;

@org.springframework.stereotype.Repository("participantRepository")
public interface Repository extends org.springframework.data.repository.Repository<Participant, Long> {

    Participant save(Participant participant);

    List<Participant> findAll();
}
