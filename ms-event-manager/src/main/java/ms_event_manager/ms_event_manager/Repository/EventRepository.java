package ms_event_manager.ms_event_manager.Repository;

import ms_event_manager.ms_event_manager.Entity.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
}
