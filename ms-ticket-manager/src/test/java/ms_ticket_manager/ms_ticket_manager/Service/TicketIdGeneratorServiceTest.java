package ms_ticket_manager.ms_ticket_manager.Service;

import ms_ticket_manager.ms_ticket_manager.Entity.IdCounter;
import ms_ticket_manager.ms_ticket_manager.Repository.IdCounterRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TicketIdGeneratorServiceTest {

    @Mock
    private IdCounterRepository idCounterRepository;

    @InjectMocks
    private TicketIdGeneratorService ticketIdGeneratorService;

    public TicketIdGeneratorServiceTest() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testGenerateNextTicketId_WhenCounterExists() {
        IdCounter existingCounter = new IdCounter();
        existingCounter.setId("ticketIdCounter");
        existingCounter.setCounter(10L);
        when(idCounterRepository.findById("ticketIdCounter")).thenReturn(Optional.of(existingCounter));
        String nextTicketId = ticketIdGeneratorService.generateNextTicketId();

        assertEquals("10", nextTicketId);
        verify(idCounterRepository).save(existingCounter);
        assertEquals(11L, existingCounter.getCounter());
    }
    @Test
    void testGenerateNextTicketId_WhenCounterDoesNotExist() {
        when(idCounterRepository.findById("ticketIdCounter")).thenReturn(Optional.empty());
        String nextTicketId = ticketIdGeneratorService.generateNextTicketId();

        assertEquals("1", nextTicketId);
        verify(idCounterRepository).save(any(IdCounter.class));
    }
}