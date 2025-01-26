package ms_event_manager.ms_event_manager.Service;


import ms_event_manager.ms_event_manager.Dto.EnderecoDTO;
import ms_event_manager.ms_event_manager.Entity.Event;
import ms_event_manager.ms_event_manager.Dto.EventRequestDTO;
import ms_event_manager.ms_event_manager.Dto.EventResponseDTO;
import ms_event_manager.ms_event_manager.Dto.EventUpdateDTO;
import ms_event_manager.ms_event_manager.Dto.Mapper.EventMapper;
import ms_event_manager.ms_event_manager.Exception.EventNotFoundException;
import ms_event_manager.ms_event_manager.Repository.EventRepository;
import ms_event_manager.ms_event_manager.Repository.TicketManagerClient;
import ms_event_manager.ms_event_manager.Repository.ViaCepClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {


    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private ViaCepClient viaCepClient;
    private final TicketManagerClient ticketManagerClient;


    public EventService(EventRepository eventRepository, EventMapper eventMapper, ViaCepClient viaCepClient, TicketManagerClient ticketManagerClient) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.viaCepClient = viaCepClient;
        this.ticketManagerClient = ticketManagerClient;
    }

    public EventResponseDTO createEvent(EventRequestDTO eventRequestDTO) {
        EnderecoDTO endereco = viaCepClient.consultarEndereco(eventRequestDTO.getCep());

        Event event = new Event();
        event.setEventName(eventRequestDTO.getEventName());
        event.setDateTime(eventRequestDTO.getDateTime());
        event.setCep(eventRequestDTO.getCep());
        event.setLogradouro(endereco.getLogradouro());
        event.setBairro(endereco.getBairro());
        event.setLocalidade(endereco.getLocalidade());
        event.setUf(endereco.getUf());


        eventRepository.save(event);

        return eventMapper.toResponseDTO(event);
    }
    public EventResponseDTO getEventById(String id) {
        return eventRepository.findById(id)
                .map(eventMapper::toResponseDTO)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + id));
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
    public void deleteEvent(String id) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isEmpty()) {
            throw new RuntimeException("Evento não encontrado com o ID: " + id);
        }
        boolean hasTickets = ticketManagerClient.checkTicketsByEvent(id);
        if (hasTickets) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ingressos vendidos para este evento");
        }
        eventRepository.deleteById(id);
    }
}
