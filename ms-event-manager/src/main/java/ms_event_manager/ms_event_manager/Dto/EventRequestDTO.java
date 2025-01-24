package ms_event_manager.ms_event_manager.Dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventRequestDTO {

    private String eventName;
    private LocalDateTime dateTime;
    private String cep;

    public EventRequestDTO(String eventName, LocalDateTime dateTime, String cep) {
        this.eventName = eventName;
        this.dateTime = dateTime;
        this.cep = cep;
    }

    public EventRequestDTO() {

    }
}