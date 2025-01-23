package ms_event_manager.ms_event_manager.Dto.Mapper;

import ms_event_manager.ms_event_manager.Entity.Event;
import ms_event_manager.ms_event_manager.Dto.EventRequestDTO;
import ms_event_manager.ms_event_manager.Dto.EventResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {


    public EventResponseDTO toResponseDTO(Event event) {
        EventResponseDTO dto = new EventResponseDTO();
        dto.setId(event.getId());
        dto.setEventName(event.getEventName());
        dto.setDateTime(event.getDateTime());
        dto.setCep(event.getCep());
        dto.setLogradouro(event.getLogradouro());
        dto.setBairro(event.getBairro());
        dto.setLocalidade(event.getLocalidade());
        dto.setUf(event.getUf());
        return dto;
    }
}