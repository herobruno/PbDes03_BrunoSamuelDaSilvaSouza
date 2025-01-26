package ms_event_manager.ms_event_manager.Service;


import ms_event_manager.ms_event_manager.Entity.IdCounter;
import ms_event_manager.ms_event_manager.Repository.IdCounterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class IdCounterServiceTest {

    @Mock
    private IdCounterRepository idCounterRepository;

    @InjectMocks
    private IdCounterService idCounterService;

    private IdCounter idCounter;

    @BeforeEach
    void setUp() {
        idCounter = new IdCounter("eventId", 10L);
    }

    @Test
    void testGenerateNextEventIdWhenCounterExists() {
        when(idCounterRepository.findById("eventId")).thenReturn(java.util.Optional.of(idCounter));
        String result = idCounterService.generateNextEventId();
        assertEquals("1", result);
        Mockito.verify(idCounterRepository).save(idCounter);
    }

    @Test
    void testGenerateNextEventIdWhenCounterDoesNotExist() {
        when(idCounterRepository.findById("eventId")).thenReturn(java.util.Optional.empty());
        String result = idCounterService.generateNextEventId();
        assertEquals("1", result);
        Mockito.verify(idCounterRepository).save(Mockito.any(IdCounter.class));
    }
}
