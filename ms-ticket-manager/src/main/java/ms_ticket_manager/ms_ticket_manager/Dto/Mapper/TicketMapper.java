package ms_ticket_manager.ms_ticket_manager.Dto.Mapper;

import ms_ticket_manager.ms_ticket_manager.Dto.TicketResponseDTO;
import ms_ticket_manager.ms_ticket_manager.Entity.Ticket;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public TicketResponseDTO toResponseDTO(Ticket ticket) {
        TicketResponseDTO dto = new TicketResponseDTO();
        dto.setTicketId(ticket.getTicketId());
        dto.setCustomerName(ticket.getCustomerName());
        dto.setCpf(ticket.getCpf());
        dto.setCustomerMail(ticket.getCustomerMail());
        dto.setEventId(ticket.getEventId());
        dto.setEventName(ticket.getEventName());
        dto.setDateTime(ticket.getDateTime());
        dto.setLogradouro(ticket.getLogradouro());
        dto.setBairro(ticket.getBairro());
        dto.setLocalidade(ticket.getLocalidade());
        dto.setUf(ticket.getUf());
        dto.setStatus(ticket.getStatus());
        dto.setBrlTotalAmount(ticket.getBRLtotalAmoun());
        dto.setUsdTotalAmount(ticket.getUSDtotalAmount());
        return dto;
    }
}