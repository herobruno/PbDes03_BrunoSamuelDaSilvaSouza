package ms_event_manager.ms_event_manager.Dto.Mapper;

import ms_event_manager.ms_event_manager.Entity.Event;
import ms_event_manager.ms_event_manager.Dto.EventRequestDTO;
import ms_event_manager.ms_event_manager.Dto.EventResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {


    public Event toEntity(EventRequestDTO dto) {
        Event event = new Event();
        event.setEventName(dto.getEventName());
        event.setDateTime(dto.getDateTime());
        event.setCep(dto.getCep());
        return event;
    }


    public EventResponseDTO toResponseDTO(Event event) {
        EventResponseDTO dto = new EventResponseDTO();
        dto.setId(event.getId());
        dto.setEventName(event.getEventName());
        dto.setDateTime(event.getDateTime());
        dto.setCep(event.getCep());
        return dto;
    }
}