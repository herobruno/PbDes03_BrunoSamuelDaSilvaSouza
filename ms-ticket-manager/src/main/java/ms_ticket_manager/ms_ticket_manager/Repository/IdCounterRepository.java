package ms_ticket_manager.ms_ticket_manager.Repository;

import ms_ticket_manager.ms_ticket_manager.Entity.IdCounter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdCounterRepository extends MongoRepository<IdCounter, String> {
}