package ms_ticket_manager.ms_ticket_manager.Service;

import ms_ticket_manager.ms_ticket_manager.Entity.IdCounter;
import ms_ticket_manager.ms_ticket_manager.Repository.IdCounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketIdGeneratorService {

    @Autowired
    private IdCounterRepository idCounterRepository;
    public String generateNextTicketId() {

        IdCounter idCounter = idCounterRepository.findById("ticketIdCounter")
                .orElseGet(() -> {
                    IdCounter newCounter = new IdCounter();
                    newCounter.setId("ticketIdCounter");
                    newCounter.setCounter(1L);
                    return newCounter;
                });
                    Long nextId = idCounter.getCounter();
                    idCounter.setCounter(nextId + 1);
                    idCounterRepository.save(idCounter);
                    return String.valueOf(nextId);
    }
}