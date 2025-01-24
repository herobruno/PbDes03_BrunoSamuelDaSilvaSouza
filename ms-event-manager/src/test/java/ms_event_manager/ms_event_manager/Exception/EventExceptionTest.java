package ms_event_manager.ms_event_manager.Exception;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class EventExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHandleEventNotFound() throws Exception {
        mockMvc.perform(get("/api/get-event/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Event not found"))
                .andExpect(jsonPath("$.message").value("Event not found with id: 1"));
    }
}
