package ms_ticket_manager.ms_ticket_manager.Service;

import ms_ticket_manager.ms_ticket_manager.Exception.TicketNotFoundException;
import ms_ticket_manager.ms_ticket_manager.Dto.TicketRequestDTO;
import ms_ticket_manager.ms_ticket_manager.Dto.TicketResponseDTO;
import ms_ticket_manager.ms_ticket_manager.Dto.EventResponseDTO;
import ms_ticket_manager.ms_ticket_manager.Entity.Ticket;
import ms_ticket_manager.ms_ticket_manager.Repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.time.LocalDateTime;
import java.util.ArrayList;
@SpringBootTest
public class TicketServiceTest {
    @Autowired
    private TicketService ticketService;
    @MockBean
    private TicketRepository ticketRepository;
    @Mock
    private RabbitTemplate rabbitTemplate;
    private TicketRequestDTO ticketRequestDTO;
    private EventResponseDTO eventResponseDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        ticketRequestDTO = new TicketRequestDTO();
        ticketRequestDTO.setCustomerName("John Doe");
        ticketRequestDTO.setCpf("12345678901");
        ticketRequestDTO.setCustomerMail("john@example.com");
        ticketRequestDTO.setEventId("1");
        ticketRequestDTO.setEventName("Roberto Carlos");
        ticketRequestDTO.setBrlAmount("100.00");
        ticketRequestDTO.setUsdAmount("20.00");

        eventResponseDTO = new EventResponseDTO();
        eventResponseDTO.setDateTime("2025-01-01T20:00:00");
        eventResponseDTO.setLogradouro("Rua Exemplo");
        eventResponseDTO.setBairro("Centro");
        eventResponseDTO.setLocalidade("São Paulo");
        eventResponseDTO.setUf("SP");
    }
    @Test
    public void testCreateTicket() {
        Ticket savedTicket = new Ticket();
        savedTicket.setTicketId("1");
        savedTicket.setCustomerName("John Doe");
        savedTicket.setCpf("12345678901");
        savedTicket.setCustomerMail("john@example.com");
        savedTicket.setEventId("1");
        savedTicket.setEventName("Roberto Carlos");
        savedTicket.setDateTime(LocalDateTime.now());
        savedTicket.setStatus("concluído");


        when(ticketRepository.save(Mockito.any(Ticket.class))).thenReturn(savedTicket);

        TicketRequestDTO ticketRequestDTO = new TicketRequestDTO();
        EventResponseDTO eventResponseDTO = new EventResponseDTO();

        TicketResponseDTO ticketResponseDTO = ticketService.createTicket(ticketRequestDTO, eventResponseDTO);

        assertNotNull(ticketResponseDTO);
        assertEquals("1", ticketResponseDTO.getTicketId());
        assertEquals("John Doe", ticketResponseDTO.getCustomerName());
        assertEquals("12345678901", ticketResponseDTO.getCpf());
        assertEquals("john@example.com", ticketResponseDTO.getCustomerMail());
        assertEquals("Roberto Carlos", ticketResponseDTO.getEventName());
        assertEquals("concluído", ticketResponseDTO.getStatus());

    }
    @Test
    public void testFindById_ShouldThrowTicketNotFoundException() {
        String invalidTicketId = "nonexistent-id";
        when(ticketRepository.findById(invalidTicketId)).thenReturn(java.util.Optional.empty());
        assertThrows(TicketNotFoundException.class, () -> ticketService.findById(invalidTicketId));
    }
    @Test
    public void testFindById_ShouldReturnTicket() {
        Ticket ticket = new Ticket();
        ticket.setTicketId("1");
        ticket.setCustomerName("John Doe");

        when(ticketRepository.findById("1")).thenReturn(java.util.Optional.of(ticket));

        Ticket foundTicket = ticketService.findById("1");
        assertNotNull(foundTicket);
        assertEquals("1", foundTicket.getTicketId());
    }
    @Test
    public void testUpdateTicket_ShouldUpdateTicket() {
        String ticketId = "1";
        TicketResponseDTO ticketResponseDTO = new TicketResponseDTO();
        ticketResponseDTO.setCustomerName("Updated Name");
        ticketResponseDTO.setCpf("98765432100");

        Ticket existingTicket = new Ticket();
        existingTicket.setTicketId(ticketId);
        existingTicket.setCustomerName("Old Name");
        existingTicket.setCpf("12345678901");

        when(ticketRepository.findById(ticketId)).thenReturn(java.util.Optional.of(existingTicket));
        when(ticketRepository.save(Mockito.any(Ticket.class))).thenReturn(existingTicket);

        Ticket updatedTicket = ticketService.updateTicket(ticketId, ticketResponseDTO);

        assertNotNull(updatedTicket);
        assertEquals("Updated Name", updatedTicket.getCustomerName());
        assertEquals("98765432100", updatedTicket.getCpf());
    }
    @Test
    public void testCancelTicket_ShouldCancelTicket() {
        String ticketId = "1";

        Ticket ticket = new Ticket();
        ticket.setTicketId(ticketId);
        ticket.setStatus("concluído");

        when(ticketRepository.findById(ticketId)).thenReturn(java.util.Optional.of(ticket));
        when(ticketRepository.save(Mockito.any(Ticket.class))).thenReturn(ticket);

        Ticket canceledTicket = ticketService.cancelTicket(ticketId);
        assertNotNull(canceledTicket);
        assertEquals("cancelado", canceledTicket.getStatus());
    }
    @Test
    void cancelTicketsByCpf_ShouldThrowException_WhenNoTicketsFound() {
        String cpf = "12345678900";
        when(ticketRepository.findByCpf(cpf)).thenReturn(new ArrayList<>());
        assertThrows(TicketNotFoundException.class, () -> ticketService.cancelTicketsByCpf(cpf));
        verify(ticketRepository, never()).saveAll(anyList());
    }
    @Test
    void getTicketsByCpf_ShouldThrowException_WhenNoTicketsFound() {
        String cpf = "12345678900";
        when(ticketRepository.findByCpf(cpf)).thenReturn(new ArrayList<>());
        TicketNotFoundException exception = assertThrows(TicketNotFoundException.class,
                () -> ticketService.getTicketsByCpf(cpf));
        assertEquals("Nenhum ingresso encontrado para o CPF informado: " + cpf, exception.getMessage());
    }
    @Test
    void getTicketsByEventId_ShouldThrowException_WhenNoTicketsFound() {
        String eventId = "E1";
        when(ticketRepository.findByEventId(eventId)).thenReturn(new ArrayList<>());
        TicketNotFoundException exception = assertThrows(TicketNotFoundException.class,
                () -> ticketService.getTicketsByEventId(eventId));
        assertEquals("Nenhum ingresso encontrado para o evento com ID: " + eventId, exception.getMessage());
    }
}

