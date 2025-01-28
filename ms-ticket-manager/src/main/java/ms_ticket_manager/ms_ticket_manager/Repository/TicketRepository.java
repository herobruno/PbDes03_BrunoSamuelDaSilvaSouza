package ms_ticket_manager.ms_ticket_manager.Repository;

import ms_ticket_manager.ms_ticket_manager.Entity.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {
    List<Ticket> findByCpf(String cpf);
    List<Ticket> findByEventId(String eventId);
}