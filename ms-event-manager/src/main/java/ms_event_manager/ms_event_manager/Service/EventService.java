package ms_event_manager.ms_event_manager.Service;


import ms_event_manager.ms_event_manager.Entity.Event;
import ms_event_manager.ms_event_manager.Dto.EventRequestDTO;
import ms_event_manager.ms_event_manager.Dto.EventResponseDTO;
import ms_event_manager.ms_event_manager.Dto.EventUpdateDTO;
import ms_event_manager.ms_event_manager.Dto.Mapper.EventMapper;
import ms_event_manager.ms_event_manager.Repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public EventService(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    public EventResponseDTO createEvent(EventRequestDTO requestDTO) {
        Event event = eventMapper.toEntity(requestDTO);
        Event savedEvent = eventRepository.save(event);
        return eventMapper.toResponseDTO(savedEvent);
    }
    public EventResponseDTO getEventById(String id) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isPresent()) {
            return eventMapper.toResponseDTO(eventOptional.get());
        } else {
            throw new RuntimeException("Event not found with id: " + id);
        }
    }
    public List<EventResponseDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(eventMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    public List<EventResponseDTO> getAllEventsSorted() {
        List<Event> events = eventRepository.findAll();

        events.sort((event1, event2) -> event1.getEventName().compareToIgnoreCase(event2.getEventName()));

        return events.stream()
                .map(eventMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public EventResponseDTO updateEvent(String id, EventUpdateDTO eventUpdateDTO) {
        Optional<Event> eventOptional = eventRepository.findById(id);

        if (eventOptional.isEmpty()) {
            throw new RuntimeException("Evento não encontrado com o ID: " + id);
        }

        Event event = eventOptional.get();

        if (eventUpdateDTO.getEventName() != null) {
            event.setEventName(eventUpdateDTO.getEventName());
        }
        if (eventUpdateDTO.getDateTime() != null) {
            event.setDateTime(eventUpdateDTO.getDateTime());
        }
        if (eventUpdateDTO.getCep() != null) {
            event.setCep(eventUpdateDTO.getCep());
        }

        Event updatedEvent = eventRepository.save(event);

        return eventMapper.toResponseDTO(updatedEvent);
    }
}
