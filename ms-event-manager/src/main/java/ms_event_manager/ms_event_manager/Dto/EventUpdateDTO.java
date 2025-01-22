package ms_event_manager.ms_event_manager.Dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventUpdateDTO {

    private String eventName;

    private LocalDateTime dateTime;

    private String cep;
}
