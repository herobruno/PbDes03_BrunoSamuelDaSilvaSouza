package ms_ticket_manager.ms_ticket_manager.Controller;

import ms_ticket_manager.ms_ticket_manager.Dto.EventResponseDTO;
import ms_ticket_manager.ms_ticket_manager.Dto.TicketRequestDTO;
import ms_ticket_manager.ms_ticket_manager.Dto.TicketResponseDTO;
import ms_ticket_manager.ms_ticket_manager.Dto.Mapper.TicketMapper;
import ms_ticket_manager.ms_ticket_manager.Entity.Ticket;
import ms_ticket_manager.ms_ticket_manager.Repository.EventFeignClient;
import ms_ticket_manager.ms_ticket_manager.Service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




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

    @PostMapping("/tickets")
    public TicketResponseDTO createTicket(@RequestBody TicketRequestDTO ticketRequestDTO) {
        EventResponseDTO eventResponseDTO = eventFeignClient.getEventById(ticketRequestDTO.getEventId());
        return ticketService.createTicket(ticketRequestDTO, eventResponseDTO);
    }
    @GetMapping("/get-ticket/{id}")
    public ResponseEntity<TicketResponseDTO> getTicketById(@PathVariable String id) {
        Ticket ticket = ticketService.findById(id);
        TicketResponseDTO responseDTO = ticketMapper.toResponseDTO(ticket);
        return ResponseEntity.ok(responseDTO);
    }
}