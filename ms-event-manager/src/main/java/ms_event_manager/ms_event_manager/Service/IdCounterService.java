package ms_event_manager.ms_event_manager.Service;

import ms_event_manager.ms_event_manager.Entity.IdCounter;
import ms_event_manager.ms_event_manager.Repository.IdCounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdCounterService {

    @Autowired
    private IdCounterRepository idCounterRepository;

    public String generateNextEventId() {
        IdCounter idCounter = idCounterRepository.findById("eventId").orElse(new IdCounter("eventId", 0L));
        Long currentCounter = idCounter.getCounter() != null ? idCounter.getCounter() : 0L;
        Long nextId = currentCounter + 1;
        idCounter.setCounter(nextId);
        idCounterRepository.save(idCounter);
        return String.valueOf(nextId);
    }
}
