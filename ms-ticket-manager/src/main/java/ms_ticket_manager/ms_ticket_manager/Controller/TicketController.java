package ms_ticket_manager.ms_ticket_manager.Controller;

import ms_ticket_manager.ms_ticket_manager.Dto.EventResponseDTO;
import ms_ticket_manager.ms_ticket_manager.Dto.TicketRequestDTO;
import ms_ticket_manager.ms_ticket_manager.Dto.TicketResponseDTO;
import ms_ticket_manager.ms_ticket_manager.Dto.Mapper.TicketMapper;
import ms_ticket_manager.ms_ticket_manager.Entity.Ticket;
import ms_ticket_manager.ms_ticket_manager.Exception.TicketNotFoundException;
import ms_ticket_manager.ms_ticket_manager.Repository.EventFeignClient;
import ms_ticket_manager.ms_ticket_manager.Service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class TicketController {

    @Autowired
    private TicketService ticketService;
    private final TicketMapper ticketMapper;
    @Autowired
    private EventFeignClient eventFeignClient;

    public TicketController(TicketMapper ticketMapper) {
        this.ticketMapper = ticketMapper;
    }

    @PostMapping("/create-ticket")
    public ResponseEntity<TicketResponseDTO> createTicket(@RequestBody TicketRequestDTO ticketRequestDTO) {
        EventResponseDTO eventResponseDTO = eventFeignClient.getEventById(ticketRequestDTO.getEventId());
        TicketResponseDTO ticketResponseDTO = ticketService.createTicket(ticketRequestDTO, eventResponseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketResponseDTO);
    }

    @GetMapping("/get-ticket/{id}")
    public ResponseEntity<TicketResponseDTO> getTicketById(@PathVariable String id) {
        Ticket ticket = ticketService.findById(id);
        TicketResponseDTO responseDTO = ticketMapper.toResponseDTO(ticket);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/update-ticket/{id}")
    public ResponseEntity<TicketResponseDTO> updateTicket(@PathVariable("id") String id, @RequestBody TicketResponseDTO ticketResponseDTO) {
        try {
            Ticket updatedTicket = ticketService.updateTicket(id, ticketResponseDTO);

            TicketResponseDTO responseDTO = new TicketResponseDTO();
            responseDTO.setTicketId(updatedTicket.getTicketId());
            responseDTO.setCustomerName(updatedTicket.getCustomerName());
            responseDTO.setCpf(updatedTicket.getCpf());
            responseDTO.setCustomerMail(updatedTicket.getCustomerMail());
            responseDTO.setEventId(updatedTicket.getEventId());
            responseDTO.setEventName(updatedTicket.getEventName());
            responseDTO.setDateTime(updatedTicket.getDateTime());
            responseDTO.setLogradouro(updatedTicket.getLogradouro());
            responseDTO.setBairro(updatedTicket.getBairro());
            responseDTO.setLocalidade(updatedTicket.getLocalidade());
            responseDTO.setUf(updatedTicket.getUf());
            responseDTO.setStatus(updatedTicket.getStatus());
            responseDTO.setBrlTotalAmount(updatedTicket.getBRLtotalAmoun());
            responseDTO.setUsdTotalAmount(updatedTicket.getUSDtotalAmount());

            return ResponseEntity.ok(responseDTO);

        } catch (TicketNotFoundException ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/cancel-ticket/{id}")
    public ResponseEntity<String> cancelTicket(@PathVariable("id") String id) {
        try {
            ticketService.cancelTicket(id);
            return ResponseEntity.ok("Ingresso cancelado com sucesso.");
        } catch (TicketNotFoundException ex) {
            return new ResponseEntity<>("Ingresso não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/cancel-ticket/cpf/{cpf}")
    public ResponseEntity<String> cancelTicketsByCpf(@PathVariable("cpf") String cpf) {
        try {
            ticketService.cancelTicketsByCpf(cpf);
            return ResponseEntity.ok("Todos os ingressos associados ao CPF " + cpf + " foram cancelados com sucesso.");
        } catch (TicketNotFoundException ex) {
            return new ResponseEntity<>("Nenhum ingresso encontrado para o CPF informado: " + cpf, HttpStatus.NOT_FOUND);
        }
    }
}