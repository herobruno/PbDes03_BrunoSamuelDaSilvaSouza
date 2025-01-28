package ms_event_manager.ms_event_manager.Repository;


import ms_event_manager.ms_event_manager.Entity.IdCounter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IdCounterRepository extends MongoRepository<IdCounter, String> {
}