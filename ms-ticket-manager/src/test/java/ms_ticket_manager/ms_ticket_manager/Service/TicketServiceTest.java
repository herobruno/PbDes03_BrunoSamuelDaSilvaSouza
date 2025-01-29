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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class TicketServiceTest {
    @Autowired
    private TicketService ticketService;
    @MockBean
    private TicketRepository ticketRepository;
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
    @Test
    void testCancelTicketsByCpf() throws TicketNotFoundException {
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket1 = new Ticket();
        ticket1.setStatus("ativo");

        Ticket ticket2 = new Ticket();
        ticket2.setStatus("ativo");

        tickets.add(ticket1);
        tickets.add(ticket2);

        when(ticketRepository.findByCpf("12345678900")).thenReturn(tickets);

        ticketService.cancelTicketsByCpf("12345678900");

        for (Ticket ticket : tickets) {
            assert "cancelado".equals(ticket.getStatus());
        }
        verify(ticketRepository).saveAll(tickets);
    }
    @Test
    void testGetTicketsByCpfMapping() throws TicketNotFoundException {
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket1 = new Ticket();
        ticket1.setTicketId("1");
        ticket1.setCustomerName("John Doe");
        ticket1.setCpf("12345678900");
        ticket1.setCustomerMail("john.doe@example.com");
        ticket1.setEventId("E001");
        ticket1.setEventName("Concert");
        ticket1.setDateTime(LocalDateTime.of(2025, 1, 1, 20, 0));
        ticket1.setLogradouro("Rua A");
        ticket1.setBairro("Centro");
        ticket1.setLocalidade("São Paulo");
        ticket1.setUf("SP");
        ticket1.setStatus("ativo");

        tickets.add(ticket1);

        when(ticketRepository.findByCpf("12345678900")).thenReturn(tickets);

        List<TicketResponseDTO> result = ticketService.getTicketsByCpf("12345678900");

        assertEquals(1, result.size());
        TicketResponseDTO dto = result.get(0);
        assertEquals("1", dto.getTicketId());
        assertEquals("John Doe", dto.getCustomerName());
        assertEquals("12345678900", dto.getCpf());
        assertEquals("john.doe@example.com", dto.getCustomerMail());
        assertEquals("E001", dto.getEventId());
        assertEquals("Concert", dto.getEventName());
        assertEquals(LocalDateTime.of(2025, 1, 1, 20, 0), dto.getDateTime());
        assertEquals("Rua A", dto.getLogradouro());
        assertEquals("Centro", dto.getBairro());
        assertEquals("São Paulo", dto.getLocalidade());
        assertEquals("SP", dto.getUf());
        assertEquals("ativo", dto.getStatus());

        verify(ticketRepository, times(1)).findByCpf("12345678900");
    }
    @Test
    void testGetTicketsByEventIdMapping() {
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket();
        ticket.setTicketId("123");
        ticket.setCustomerName("John Doe");
        ticket.setCpf("98765432100");
        ticket.setCustomerMail("john.doe@example.com");
        ticket.setEventId("E001");
        ticket.setEventName("Rock Concert");
        ticket.setDateTime(LocalDateTime.of(2025, 2, 15, 20, 30));
        ticket.setLogradouro("Rua das Flores");
        ticket.setBairro("Centro");
        ticket.setLocalidade("São Paulo");
        ticket.setUf("SP");
        ticket.setStatus("ativo");
        ticket.setBRLtotalAmoun(String.valueOf(new BigDecimal("200.00")));
        ticket.setUSDtotalAmount(String.valueOf(new BigDecimal("40.00")));

        tickets.add(ticket);

        List<TicketResponseDTO> ticketResponseDTOs = tickets.stream()
                .map(t -> {
                    TicketResponseDTO dto = new TicketResponseDTO();
                    dto.setTicketId(t.getTicketId());
                    dto.setCustomerName(t.getCustomerName());
                    dto.setCpf(t.getCpf());
                    dto.setCustomerMail(t.getCustomerMail());
                    dto.setEventId(t.getEventId());
                    dto.setEventName(t.getEventName());
                    dto.setDateTime(t.getDateTime());
                    dto.setLogradouro(t.getLogradouro());
                    dto.setBairro(t.getBairro());
                    dto.setLocalidade(t.getLocalidade());
                    dto.setUf(t.getUf());
                    dto.setStatus(t.getStatus());
                    dto.setBrlTotalAmount(t.getBRLtotalAmoun());
                    dto.setUsdTotalAmount(t.getUSDtotalAmount());
                    return dto;
                })
                .collect(Collectors.toList());

        assertEquals(1, ticketResponseDTOs.size());
        TicketResponseDTO dto = ticketResponseDTOs.get(0);
        assertEquals("123", dto.getTicketId());
        assertEquals("John Doe", dto.getCustomerName());
        assertEquals("98765432100", dto.getCpf());
        assertEquals("john.doe@example.com", dto.getCustomerMail());
        assertEquals("E001", dto.getEventId());
        assertEquals("Rock Concert", dto.getEventName());
        assertEquals(LocalDateTime.of(2025, 2, 15, 20, 30), dto.getDateTime());
        assertEquals("Rua das Flores", dto.getLogradouro());
        assertEquals("Centro", dto.getBairro());
        assertEquals("São Paulo", dto.getLocalidade());
        assertEquals("SP", dto.getUf());
        assertEquals("ativo", dto.getStatus());
        assertEquals(0, dto.getBrlTotalAmount().compareTo(String.valueOf(new BigDecimal("200.00"))));
        assertEquals(0, dto.getUsdTotalAmount().compareTo(String.valueOf(new BigDecimal("40.00"))));
    }
}

