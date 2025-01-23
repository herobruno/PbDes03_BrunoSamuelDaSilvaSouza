package ms_ticket_manager.ms_ticket_manager.Controller;

import ms_ticket_manager.ms_ticket_manager.Dto.EventResponseDTO;
import ms_ticket_manager.ms_ticket_manager.Dto.TicketRequestDTO;
import ms_ticket_manager.ms_ticket_manager.Dto.TicketResponseDTO;
import ms_ticket_manager.ms_ticket_manager.Repository.EventFeignClient;
import ms_ticket_manager.ms_ticket_manager.Service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/api")
public class TicketController {

    @Autowired
    private TicketService ticketService;
    @Autowired
    private EventFeignClient eventFeignClient;
    @PostMapping("/tickets")
    public TicketResponseDTO createTicket(@RequestBody TicketRequestDTO ticketRequestDTO) {


        EventResponseDTO eventResponseDTO = eventFeignClient.getEventById(ticketRequestDTO.getEventId());

        return ticketService.createTicket(ticketRequestDTO, eventResponseDTO);
    }
}