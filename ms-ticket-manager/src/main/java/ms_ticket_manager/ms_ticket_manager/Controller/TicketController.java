package ms_ticket_manager.ms_ticket_manager.Controller;

import ms_ticket_manager.ms_ticket_manager.Dto.TicketRequestDTO;
import ms_ticket_manager.ms_ticket_manager.Dto.TicketResponseDTO;
import ms_ticket_manager.ms_ticket_manager.Entity.Ticket;
import ms_ticket_manager.ms_ticket_manager.Service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/create-ticket")
    public ResponseEntity<TicketResponseDTO> createTicket(@RequestBody TicketRequestDTO ticketRequestDTO) {

        Ticket ticket = new Ticket();
        ticket.setCustomerName(ticketRequestDTO.getCustomerName());
        ticket.setCpf(ticketRequestDTO.getCpf());
        ticket.setCustomerMail(ticketRequestDTO.getCustomerMail());
        ticket.setEventId(ticketRequestDTO.getEventId());
        ticket.setEventName(ticketRequestDTO.getEventName());
        ticket.setBrlAmount(ticketRequestDTO.getBrlAmount());
        ticket.setUsdAmount(ticketRequestDTO.getUsdAmount());
        ticket.setStatus("concluído");


        Ticket savedTicket = ticketService.createTicket(ticket);


        TicketResponseDTO ticketResponseDTO = new TicketResponseDTO();
        ticketResponseDTO.setTicketId(savedTicket.getTicketId());
        ticketResponseDTO.setCustomerName(savedTicket.getCustomerName());
        ticketResponseDTO.setCpf(savedTicket.getCpf());
        ticketResponseDTO.setCustomerMail(savedTicket.getCustomerMail());
        ticketResponseDTO.setEventId(savedTicket.getEventId());
        ticketResponseDTO.setEventName(savedTicket.getEventName());
        ticketResponseDTO.setBrlTotalAmount(savedTicket.getBrlAmount());
        ticketResponseDTO.setUsdTotalAmount(savedTicket.getUsdAmount());
        ticketResponseDTO.setStatus(savedTicket.getStatus());


        return ResponseEntity.ok(ticketResponseDTO);
    }
}
