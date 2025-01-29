package ms_event_manager.ms_event_manager.Exception;

import ms_event_manager.ms_event_manager.Service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EventExceptionTest {
    @MockBean
    private EventService eventService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void handleEventNotFound_ReturnsNotFoundResponse() throws Exception {
        String eventId = "1";
        when(eventService.getEventById(eventId))
                .thenThrow(new EventNotFoundException("Event not found with id: " + eventId));
        mockMvc.perform(get("/api/get-event/{id}", eventId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Event not found"))
                .andExpect(jsonPath("$.message").value("Event not found with id: " + eventId));
    }
}
