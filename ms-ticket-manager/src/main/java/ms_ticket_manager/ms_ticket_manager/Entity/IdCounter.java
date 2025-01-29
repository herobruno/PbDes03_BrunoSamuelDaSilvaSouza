package ms_ticket_manager.ms_ticket_manager.Entity;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
@Data
@Entity
public class IdCounter {
    @Id
    private String id;
    private Long counter;
}