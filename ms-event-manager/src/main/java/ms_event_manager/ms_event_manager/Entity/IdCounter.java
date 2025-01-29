package ms_event_manager.ms_event_manager.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class IdCounter {
    @Id
    private String id;
    private Long counter;

    public IdCounter(String id, long counter) {
        this.id = id;
        this.counter = counter;
    }

    public IdCounter() {
    }
}
