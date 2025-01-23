package ms_event_manager.ms_event_manager.Service;


import ms_event_manager.ms_event_manager.Dto.EnderecoDTO;
import ms_event_manager.ms_event_manager.Entity.Event;
import ms_event_manager.ms_event_manager.Dto.EventRequestDTO;
import ms_event_manager.ms_event_manager.Dto.EventResponseDTO;
import ms_event_manager.ms_event_manager.Dto.EventUpdateDTO;
import ms_event_manager.ms_event_manager.Dto.Mapper.EventMapper;
import ms_event_manager.ms_event_manager.Repository.EventRepository;
import ms_event_manager.ms_event_manager.Repository.ViaCepClient;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue eventQueue;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private ViaCepClient viaCepClient;



    public void sendEventToTicketService(String eventMessage) {
        rabbitTemplate.convertAndSend(eventQueue.getName(), eventMessage);
    }
    public EventService(EventRepository eventRepository, EventMapper eventMapper, ViaCepClient viaCepClient) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.viaCepClient = viaCepClient;
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
    public void deleteEvent(String id) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isEmpty()) {
            throw new RuntimeException("Evento não encontrado com o ID: " + id);
        }
        eventRepository.deleteById(id);
    }
}
