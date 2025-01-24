package ms_ticket_manager.ms_ticket_manager.Repository;

import ms_ticket_manager.ms_ticket_manager.Entity.Ticket;
import ms_ticket_manager.ms_ticket_manager.Service.TicketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketRepositoryTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;
    @Test
    public void shouldSaveAndFindTicket() {
        Ticket ticket = new Ticket();
        ticket.setTicketId("TICKET123");
        ticket.setCustomerName("John Doe");
        ticket.setCpf("123.456.789-00");
        ticket.setCustomerMail("johndoe@example.com");
        ticket.setEventId("EVT001");
        ticket.setEventName("Rock Concert");
        ticket.setBRLtotalAmoun("200.00");
        ticket.setUSDtotalAmount("40.00");
        ticket.setStatus("CONFIRMED");

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        Ticket savedTicket = ticketRepository.save(ticket);

        assertThat(savedTicket).isNotNull();
        assertThat(savedTicket.getTicketId()).isEqualTo("TICKET123");

        when(ticketRepository.findById(ticket.getTicketId())).thenReturn(Optional.of(ticket));

        Optional<Ticket> foundTicket = ticketRepository.findById(ticket.getTicketId());
        assertThat(foundTicket).isPresent();
        assertThat(foundTicket.get().getCustomerName()).isEqualTo("John Doe");

        verify(ticketRepository, times(1)).findById(ticket.getTicketId());
    }

    @Test
    public void shouldDeleteTicket() {
        Ticket ticket = new Ticket();
        ticket.setTicketId("TICKET456");
        ticket.setCustomerName("Jane Smith");
        ticket.setCpf("987.654.321-00");
        ticket.setCustomerMail("janesmith@example.com");
        ticket.setEventId("EVT002");
        ticket.setEventName("Jazz Night");
        ticket.setBRLtotalAmoun("150.00");
        ticket.setUSDtotalAmount("30.00");
        ticket.setStatus("PENDING");

        doNothing().when(ticketRepository).delete(ticket);  // Mock do método delete

        ticketRepository.delete(ticket);

        verify(ticketRepository, times(1)).delete(ticket);
    }
}