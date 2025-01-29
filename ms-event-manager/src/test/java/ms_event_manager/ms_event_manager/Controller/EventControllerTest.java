package ms_event_manager.ms_event_manager.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ms_event_manager.ms_event_manager.Dto.EventRequestDTO;
import ms_event_manager.ms_event_manager.Dto.EventResponseDTO;
import ms_event_manager.ms_event_manager.Dto.EventUpdateDTO;
import ms_event_manager.ms_event_manager.Exception.EventNotFoundException;
import ms_event_manager.ms_event_manager.Service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EventControllerTest {
    private MockMvc mockMvc;
    @Mock
    private EventService eventService;
    @InjectMocks
    private EventController eventController;

    private EventResponseDTO eventResponseDTO;
    private EventRequestDTO eventRequestDTO;
    private EventUpdateDTO eventUpdateDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
        eventResponseDTO = new EventResponseDTO("1", "Sample Event", LocalDateTime.of(2025, 5, 1, 15, 0),
                "12345-678", "Rua Teste", "Bairro Teste", "Cidade Teste", "SP");
        eventRequestDTO = new EventRequestDTO("Sample Event", LocalDateTime.of(2025, 5, 1, 15, 0), "12345-678");
        eventUpdateDTO = new EventUpdateDTO("Updated Event", LocalDateTime.of(2025, 6, 1, 16, 0), "12345-678");
    }

    @Test
    void testCreateEvent() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        EventRequestDTO eventRequestDTO = new EventRequestDTO("Sample Event", LocalDateTime.of(2025, 5, 1, 15, 0, 0, 0), "12345-678");
        when(eventService.createEvent(any(EventRequestDTO.class))).thenReturn(new EventResponseDTO(
                "1", "Sample Event", LocalDateTime.of(2025, 5, 1, 15, 0, 0, 0), "12345-678",
                "Rua Teste", "Bairro Teste", "Localidade Teste", "UF"
        ));

        mockMvc.perform(post("/api/create-event")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(eventRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.eventName").value("Sample Event"))
                .andExpect(jsonPath("$.dateTime").value("2025-05-01T15:00:00"))
                .andExpect(jsonPath("$.logradouro").value("Rua Teste"));
    }
    @Test
    void testGetEventById() throws Exception {
        EventResponseDTO eventResponseDTO = new EventResponseDTO(
                "1",
                "Sample Event",
                LocalDateTime.of(2025, 5, 1, 15, 0),
                "12345-678",
                "Rua Teste",
                "Bairro Teste",
                "Cidade Teste",
                "SP"
        );


        when(eventService.getEventById("1")).thenReturn(eventResponseDTO);


        mockMvc.perform(get("/api/get-event/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.eventName").value("Sample Event"))
                .andExpect(jsonPath("$.dateTime").value("2025-05-01T15:00:00"))
                .andExpect(jsonPath("$.logradouro").value("Rua Teste"))
                .andExpect(jsonPath("$.bairro").value("Bairro Teste"))
                .andExpect(jsonPath("$.localidade").value("Cidade Teste"))
                .andExpect(jsonPath("$.uf").value("SP"))
                .andExpect(jsonPath("$.cep").value("12345-678"));
    }
    @Test
    void testGetAllEvents() throws Exception {
        when(eventService.getAllEvents()).thenReturn(List.of(eventResponseDTO));
        mockMvc.perform(get("/api/get-all-events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].eventName").value("Sample Event"));
    }

    @Test
    void testDeleteEventNotFound() throws Exception {
        doThrow(new EventNotFoundException("Evento não encontrado")).when(eventService).deleteEvent("1");
        mockMvc.perform(delete("/api/delete-event/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Evento não encontrado"));
    }
    @Test
    void testDeleteEventSuccess() throws Exception {
        when(eventService.eventExists("1")).thenReturn(true);
        doNothing().when(eventService).deleteEvent("1");
        mockMvc.perform(delete("/api/delete-event/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

}
