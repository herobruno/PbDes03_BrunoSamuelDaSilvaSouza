package ms_event_manager.ms_event_manager.Service;

import ms_event_manager.ms_event_manager.Dto.EventRequestDTO;
import ms_event_manager.ms_event_manager.Dto.EventResponseDTO;
import ms_event_manager.ms_event_manager.Dto.EventUpdateDTO;
import ms_event_manager.ms_event_manager.Dto.EnderecoDTO;
import ms_event_manager.ms_event_manager.Entity.Event;
import ms_event_manager.ms_event_manager.Exception.EventNotFoundException;
import ms_event_manager.ms_event_manager.Repository.EventRepository;
import ms_event_manager.ms_event_manager.Repository.ViaCepClient;
import ms_event_manager.ms_event_manager.Dto.Mapper.EventMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventServiceTest {
    @Mock
    private EventRepository eventRepository;
    @Mock
    private EventMapper eventMapper;

    @Mock
    private ViaCepClient viaCepClient;
    @InjectMocks
    private EventService eventService;

    private Event event;
    private EventRequestDTO eventRequestDTO;
    private EventResponseDTO eventResponseDTO;
    private EventUpdateDTO eventUpdateDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        event = new Event();
        event.setId("1");
        event.setEventName("Sample Event");
        event.setDateTime(LocalDateTime.of(2025, 5, 1, 15, 0));
        event.setCep("12345-678");

        eventRequestDTO = new EventRequestDTO();
        eventRequestDTO.setEventName("Sample Event");
        eventRequestDTO.setDateTime(LocalDateTime.parse("2025-05-01T15:00:00"));
        eventRequestDTO.setCep("12345-678");

        eventResponseDTO = new EventResponseDTO();
        eventResponseDTO.setEventName("Sample Event");
        eventResponseDTO.setDateTime(LocalDateTime.parse("2025-05-01T15:00:00"));
        eventResponseDTO.setCep("12345-678");

        eventUpdateDTO = new EventUpdateDTO();
        eventUpdateDTO.setEventName("Updated Event");
        eventUpdateDTO.setDateTime(LocalDateTime.parse("2025-06-01T16:00:00"));
    }

    @Test
    public void testCreateEvent() {
        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setLogradouro("Rua Teste");
        enderecoDTO.setBairro("Bairro Teste");
        enderecoDTO.setLocalidade("Cidade Teste");
        enderecoDTO.setUf("SP");
        when(viaCepClient.consultarEndereco(Mockito.anyString())).thenReturn(enderecoDTO);

        Event event = new Event("1", "Event", LocalDateTime.of(2025, 5, 1, 15, 0),
                "12345-678", "Rua Teste", "Bairro Teste", "Cidade Teste", "SP");
        when(eventRepository.save(Mockito.any(Event.class))).thenReturn(event);
        EventResponseDTO eventResponseDTO = new EventResponseDTO("1", "Event", LocalDateTime.of(2025, 5, 1, 15, 0),
                "12345-678", "Rua Teste", "Bairro Teste", "Cidade Teste", "SP");
        when(eventMapper.toResponseDTO(Mockito.any(Event.class))).thenReturn(eventResponseDTO);
        EventRequestDTO eventRequestDTO = new EventRequestDTO("Sample Event", LocalDateTime.of(2025, 5, 1, 15, 0),
                "12345-678");
        EventResponseDTO response = eventService.createEvent(eventRequestDTO);

        assertNotNull(response);
        assertEquals("1", response.getId());
        assertEquals("Event", response.getEventName());
        assertEquals(LocalDateTime.of(2025, 5, 1, 15, 0), response.getDateTime());
        assertEquals("Rua Teste", response.getLogradouro());
        assertEquals("Bairro Teste", response.getBairro());
        assertEquals("Cidade Teste", response.getLocalidade());
        assertEquals("SP", response.getUf());
        assertEquals("12345-678", response.getCep());
    }

    @Test
    public void testGetEventByIdFound() {
        when(eventRepository.findById("1")).thenReturn(Optional.of(event));
        when(eventMapper.toResponseDTO(event)).thenReturn(eventResponseDTO);

        EventResponseDTO response = eventService.getEventById("1");
        assertNotNull(response);
        assertEquals("Sample Event", response.getEventName());
        assertEquals(LocalDateTime.of(2025, 5, 1, 15, 0), response.getDateTime());
    }

    @Test
    public void testGetEventByIdNotFound() {
        when(eventRepository.findById("999")).thenReturn(Optional.empty());
        EventNotFoundException exception = assertThrows(EventNotFoundException.class, () -> {
            eventService.getEventById("999");
        });
        assertEquals("Event not found with id: 999", exception.getMessage());
    }

    @Test
    public void testDeleteEvent() {
        when(eventRepository.findById("1")).thenReturn(Optional.of(event));
        eventService.deleteEvent("1");
        verify(eventRepository, times(1)).deleteById("1");
    }

    @Test
    public void testDeleteEventNotFound() {
        when(eventRepository.findById("999")).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            eventService.deleteEvent("999");
        });
        assertEquals("Evento não encontrado com o ID: 999", exception.getMessage());
    }

    @Test
    void testGetAllEventsSorted() {
        List<Event> mockEvents = new ArrayList<>(List.of(
                Event.builder()
                        .id("1")
                        .eventName("Event Z")
                        .dateTime(LocalDateTime.of(2025, 6, 2, 14, 30))
                        .cep("12345")
                        .build(),
                Event.builder()
                        .id("2")
                        .eventName("Event A")
                        .dateTime(LocalDateTime.of(2025, 6, 1, 16, 0))
                        .cep("67890")
                        .build()
        ));

        when(eventRepository.findAll()).thenReturn(mockEvents);

        when(eventMapper.toResponseDTO(mockEvents.get(0))).thenReturn(
                new EventResponseDTO("1", "Event Z", LocalDateTime.of(2025, 6, 2, 14, 30), "12345", null, null, null, null)
        );
        when(eventMapper.toResponseDTO(mockEvents.get(1))).thenReturn(
                new EventResponseDTO("2", "Event A", LocalDateTime.of(2025, 6, 1, 16, 0), "67890", null, null, null, null)
        );
        List<EventResponseDTO> result = eventService.getAllEventsSorted();

        assertEquals(2, result.size());
        assertEquals("Event A", result.get(0).getEventName());
        assertEquals("Event Z", result.get(1).getEventName());
        verify(eventRepository, times(1)).findAll();
        verify(eventMapper, times(2)).toResponseDTO(any(Event.class));
    }

    @Test
    public void testUpdateEvent_EventNotFound() {
        String eventId = "1";
        EventUpdateDTO eventUpdateDTO = new EventUpdateDTO("Updated Event", "2025-01-25T12:00", "12345");


        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            eventService.updateEvent(eventId, eventUpdateDTO);
        });
        assertEquals("Evento não encontrado com o ID: 1", exception.getMessage());
    }
    @Test
    public void testGetAllEvents() {

        Event event1 = new Event();
        event1.setEventName("Event 1");
        event1.setDateTime(LocalDateTime.parse("2025-02-01T14:00"));
        event1.setCep("12345");
        Event event2 = new Event();
        event2.setEventName("Event 2");
        event2.setDateTime(LocalDateTime.parse("2025-03-01T15:00"));
        event2.setCep("67890");
        when(eventRepository.findAll()).thenReturn(Arrays.asList(event1, event2));

        EventResponseDTO dto1 = new EventResponseDTO("1", "Event 1", LocalDateTime.parse("2025-02-01T14:00"), "12345", "Rua A", "Bairro X", "Cidade Y", "UF Z");
        EventResponseDTO dto2 = new EventResponseDTO("2", "Event 2", LocalDateTime.parse("2025-03-01T15:00"), "67890", "Rua B", "Bairro Y", "Cidade Z", "UF W");
        when(eventMapper.toResponseDTO(event1)).thenReturn(dto1);
        when(eventMapper.toResponseDTO(event2)).thenReturn(dto2);

        List<EventResponseDTO> eventsResponse = eventService.getAllEvents();

        assertNotNull(eventsResponse);
        assertEquals(2, eventsResponse.size());

        assertEquals("Event 1", eventsResponse.get(0).getEventName());
        assertEquals("2025-02-01T14:00", eventsResponse.get(0).getDateTime().toString());
        assertEquals("12345", eventsResponse.get(0).getCep());

        assertEquals("Event 2", eventsResponse.get(1).getEventName());
        assertEquals("2025-03-01T15:00", eventsResponse.get(1).getDateTime().toString());
        assertEquals("67890", eventsResponse.get(1).getCep());
    }
    @Test
    public void testUpdateEvent() {
        String eventId = "123";
        Event existingEvent = new Event();
        existingEvent.setId(eventId);
        existingEvent.setEventName("Evento Original");
        existingEvent.setDateTime(LocalDateTime.parse("2025-01-01T10:00"));
        existingEvent.setCep("12345");

        EventUpdateDTO updateDTO = new EventUpdateDTO();
        updateDTO.setEventName("Evento Atualizado");
        updateDTO.setDateTime(LocalDateTime.parse("2025-02-01T14:00"));

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(eventMapper.toResponseDTO(any(Event.class))).thenReturn(
                new EventResponseDTO(eventId, "Evento Atualizado", LocalDateTime.parse("2025-02-01T14:00"), "12345", null, null, null, null)
        );

        EventResponseDTO responseDTO = eventService.updateEvent(eventId, updateDTO);

        assertNotNull(responseDTO);
        assertEquals("Evento Atualizado", responseDTO.getEventName());
        assertEquals("2025-02-01T14:00", responseDTO.getDateTime().toString());
        verify(eventRepository).save(any(Event.class));
        verify(eventMapper).toResponseDTO(any(Event.class));
    }
}
