package ms_ticket_manager.ms_ticket_manager.Controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import ms_ticket_manager.ms_ticket_manager.Dto.EventResponseDTO;
import ms_ticket_manager.ms_ticket_manager.Dto.TicketRequestDTO;
import ms_ticket_manager.ms_ticket_manager.Dto.TicketResponseDTO;
import ms_ticket_manager.ms_ticket_manager.Entity.Ticket;
import ms_ticket_manager.ms_ticket_manager.Exception.TicketNotFoundException;
import ms_ticket_manager.ms_ticket_manager.Repository.TicketRepository;
import ms_ticket_manager.ms_ticket_manager.Service.TicketService;
import ms_ticket_manager.ms_ticket_manager.Repository.EventFeignClient;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@AutoConfigureMockMvc
@SpringBootTest
public class TicketControllerTest {
    @Mock
    private TicketRepository ticketRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @MockBean
    private EventFeignClient eventFeignClient;



    @Test
    public void shouldCreateTicket() throws Exception {
        TicketRequestDTO ticketRequestDTO = new TicketRequestDTO();
        ticketRequestDTO.setCustomerName("John Doe");
        ticketRequestDTO.setCpf("12345678900");
        ticketRequestDTO.setCustomerMail("johndoe@example.com");
        ticketRequestDTO.setEventId("event1");
        ticketRequestDTO.setEventName("Event Name");
        ticketRequestDTO.setStatus("Concluido");
        ticketRequestDTO.setBrlAmount("100.00");

        TicketResponseDTO ticketResponseDTO = new TicketResponseDTO();
        ticketResponseDTO.setTicketId("ticket123");
        ticketResponseDTO.setCustomerName("John Doe");

        when(ticketService.createTicket(any(TicketRequestDTO.class), any(EventResponseDTO.class)))
                .thenReturn(ticketResponseDTO);
        when(eventFeignClient.getEventById(anyString())).thenReturn(new EventResponseDTO());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/create-ticket")
                        .contentType("application/json")
                        .content("{\"customerName\":\"John Doe\",\"cpf\":\"12345678900\",\"customerMail\":\"johndoe@example.com\",\"eventId\":\"event1\",\"eventName\":\"Event Name\",\"status\":\"active\",\"BRLamount\":\"100.00\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ticketId").value("ticket123"))
                .andExpect(jsonPath("$.customerName").value("John Doe"));
    }

    @Test
    public void shouldGetTicketById() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setTicketId("ticket123");
        ticket.setCustomerName("John Doe");
        when(ticketService.findById(anyString())).thenReturn(ticket);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/get-ticket/ticket123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketId").value("ticket123"))
                .andExpect(jsonPath("$.customerName").value("John Doe"));
    }
    @Test
    public void shouldUpdateTicket() throws Exception {
        TicketResponseDTO ticketResponseDTO = new TicketResponseDTO();
        ticketResponseDTO.setTicketId("ticket123");
        ticketResponseDTO.setCustomerName("John Doe");
        Ticket updatedTicket = new Ticket();
        updatedTicket.setTicketId("ticket123");
        updatedTicket.setCustomerName("John Leitão");

        when(ticketService.updateTicket(anyString(), any(TicketResponseDTO.class)))
                .thenReturn(updatedTicket);
        mockMvc.perform(put("/api/update-ticket/ticket123")
                        .contentType("application/json")
                        .content("{\"ticketId\":\"ticket123\",\"customerName\":\"John Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketId").value("ticket123"))
                .andExpect(jsonPath("$.customerName").value("John Leitão"));
    }
    @Test
    public void shouldCancelTicket() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setTicketId("ticket123");
        ticket.setStatus("Concluido");

        when(ticketRepository.findById("ticket123")).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cancel-ticket/ticket123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Ingresso cancelado com sucesso."));
    }
    @Test
    public void shouldReturnNotFoundWhenCancelTicketNotFound() throws Exception {
        doThrow(new TicketNotFoundException("Ticket not found")).when(ticketService).cancelTicket(anyString());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cancel-ticket/ticket123"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Ingresso não encontrado."));
    }
    @Test
    public void shouldReturnNotFoundWhenTicketNotFound() throws Exception {
        String invalidTicketId = "";
        when(ticketService.findById(invalidTicketId)).thenThrow(new TicketNotFoundException("Ticket not found"));

        mockMvc.perform(get("/api/get-ticket/{id}", invalidTicketId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    public void shouldReturnTicketWhenFound() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setTicketId("1");
        ticket.setCustomerName("John Doe");
        when(ticketService.findById("1")).thenReturn(ticket);

        mockMvc.perform(get("/api/get-ticket/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketId").value("1"))
                .andExpect(jsonPath("$.customerName").value("John Doe"));
    }
    @Test
    void updateTicket_WhenTicketNotFound_ShouldReturn404() throws Exception {
        String ticketId = "1";
        TicketResponseDTO ticketResponseDTO = new TicketResponseDTO();

        doThrow(new TicketNotFoundException("Ticket not found with id: " + ticketId))
                .when(ticketService).updateTicket(ticketId, ticketResponseDTO);
        mockMvc.perform(put("/update-ticket/{id}", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ticketId\":\"1\",\"customerName\":\"John Doe\"}"))
                .andExpect(status().isNotFound());
    }
    @Test
    void getTicketsByCpf_WhenNoTicketsFound_ShouldReturn404() throws Exception {
        String cpf = "12345678900";
        when(ticketService.getTicketsByCpf(cpf)).thenThrow(new TicketNotFoundException("Nenhum ingresso encontrado para o CPF informado: " + cpf));
        mockMvc.perform(get("/get-ticket-by-cpf/{cpf}", cpf))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }
    @Test
    void testGetTicketsByCpf_Success() throws Exception {
        TicketResponseDTO dto = new TicketResponseDTO();
        dto.setTicketId("123");
        dto.setCustomerName("John Doe");
        dto.setCpf("98765432100");
        dto.setCustomerMail("john.doe@example.com");
        dto.setEventId("E001");
        dto.setEventName("Rock Concert");
        dto.setDateTime(LocalDateTime.of(2025, 2, 15, 20, 30));
        dto.setLogradouro("Rua das Flores");
        dto.setBairro("Centro");
        dto.setLocalidade("São Paulo");
        dto.setUf("SP");
        dto.setStatus("ativo");

        List<TicketResponseDTO> responseDTOs = Collections.singletonList(dto);

        when(ticketService.getTicketsByCpf("98765432100")).thenReturn(responseDTOs);

        mockMvc.perform(get("/api/get-ticket-by-cpf/98765432100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].ticketId").value("123"))
                .andExpect(jsonPath("$[0].customerName").value("John Doe"))
                .andExpect(jsonPath("$[0].cpf").value("98765432100"))
                .andExpect(jsonPath("$[0].eventId").value("E001"))
                .andExpect(jsonPath("$[0].eventName").value("Rock Concert"))
                .andExpect(jsonPath("$[0].logradouro").value("Rua das Flores"))
                .andExpect(jsonPath("$[0].bairro").value("Centro"))
                .andExpect(jsonPath("$[0].localidade").value("São Paulo"))
                .andExpect(jsonPath("$[0].uf").value("SP"))
                .andExpect(jsonPath("$[0].status").value("ativo"));
    }
}

