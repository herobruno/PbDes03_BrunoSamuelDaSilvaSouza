package ms_ticket_manager.ms_ticket_manager.Service;

import ms_ticket_manager.ms_ticket_manager.Exception.TicketNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ms_ticket_manager.ms_ticket_manager.Entity.Ticket;
import ms_ticket_manager.ms_ticket_manager.Repository.TicketRepository;
import ms_ticket_manager.ms_ticket_manager.Dto.TicketRequestDTO;
import ms_ticket_manager.ms_ticket_manager.Dto.TicketResponseDTO;
import ms_ticket_manager.ms_ticket_manager.Dto.EventResponseDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    @Autowired
    private TicketRepository ticketRepository;

    public TicketResponseDTO createTicket(TicketRequestDTO ticketRequestDTO, EventResponseDTO eventResponseDTO) {

        Ticket ticket = new Ticket();
        ticket.setCustomerName(ticketRequestDTO.getCustomerName());
        ticket.setCpf(ticketRequestDTO.getCpf());
        ticket.setCustomerMail(ticketRequestDTO.getCustomerMail());
        ticket.setEventId(ticketRequestDTO.getEventId());
        ticket.setEventName(ticketRequestDTO.getEventName());
        ticket.setBRLtotalAmoun(ticketRequestDTO.getBrlAmount());
        ticket.setUSDtotalAmount(ticketRequestDTO.getUsdAmount());
        ticket.setStatus("concluído");

        String eventDateTimeString = eventResponseDTO.getDateTime();
        try {
            if (eventDateTimeString != null && !eventDateTimeString.isEmpty()) {
                LocalDateTime eventDateTime = LocalDateTime.parse(eventDateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                ticket.setDateTime(eventDateTime);
                log.info("Data do evento parseada com sucesso: {}", eventDateTime);
            } else {
                ticket.setDateTime(LocalDateTime.now());
                log.warn("Data do evento não fornecida. Usando a data atual como padrão.");
            }
        } catch (DateTimeParseException e) {
            log.error("Erro ao parsear a data do evento: {}. Usando a data atual como fallback.", eventDateTimeString, e);
            ticket.setDateTime(LocalDateTime.now());
        }

        ticket.setLogradouro(eventResponseDTO.getLogradouro());
        ticket.setBairro(eventResponseDTO.getBairro());

        String localidade = eventResponseDTO.getLocalidade() != null ? eventResponseDTO.getLocalidade() : "Cidade não informada";
        String uf = eventResponseDTO.getUf() != null ? eventResponseDTO.getUf() : "UF não informada";
        ticket.setLocalidade(localidade);
        ticket.setUf(uf);

        log.info("Cidade definida no Ticket: {}", localidade);
        log.info("UF definida no Ticket: {}", uf);

        Ticket savedTicket = ticketRepository.save(ticket);

        TicketResponseDTO ticketResponseDTO = new TicketResponseDTO();
        ticketResponseDTO.setTicketId(savedTicket.getTicketId());
        ticketResponseDTO.setCustomerName(savedTicket.getCustomerName());
        ticketResponseDTO.setCpf(savedTicket.getCpf());
        ticketResponseDTO.setCustomerMail(savedTicket.getCustomerMail());
        ticketResponseDTO.setEventId(savedTicket.getEventId());
        ticketResponseDTO.setEventName(savedTicket.getEventName());
        ticketResponseDTO.setDateTime(savedTicket.getDateTime());
        ticketResponseDTO.setLogradouro(savedTicket.getLogradouro());
        ticketResponseDTO.setBairro(savedTicket.getBairro());
        ticketResponseDTO.setLocalidade(savedTicket.getLocalidade());
        ticketResponseDTO.setUf(savedTicket.getUf());
        ticketResponseDTO.setStatus(savedTicket.getStatus());
        ticketResponseDTO.setBrlTotalAmount(savedTicket.getBRLtotalAmoun());
        ticketResponseDTO.setUsdTotalAmount(savedTicket.getUSDtotalAmount());

        log.info("TicketResponseDTO criado com sucesso: {}", ticketResponseDTO);

        log.info(">>> Fim do método createTicket <<<");
        return ticketResponseDTO;
    }
    public Ticket findById(String id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found with id: " + id));
    }
    public Ticket updateTicket(String id, TicketResponseDTO ticketResponseDTO) throws TicketNotFoundException {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket não encontrado com o ID: " + id));

        ticket.setCustomerName(ticketResponseDTO.getCustomerName());
        ticket.setCpf(ticketResponseDTO.getCpf());
        ticket.setCustomerMail(ticketResponseDTO.getCustomerMail());
        ticket.setEventId(ticketResponseDTO.getEventId());
        ticket.setEventName(ticketResponseDTO.getEventName());
        ticket.setDateTime(ticketResponseDTO.getDateTime());
        ticket.setLogradouro(ticketResponseDTO.getLogradouro());
        ticket.setBairro(ticketResponseDTO.getBairro());
        ticket.setLocalidade(ticketResponseDTO.getLocalidade());
        ticket.setUf(ticketResponseDTO.getUf());
        ticket.setStatus(ticketResponseDTO.getStatus());
        ticket.setBRLtotalAmoun(ticketResponseDTO.getBrlTotalAmount());
        ticket.setUSDtotalAmount(ticketResponseDTO.getUsdTotalAmount());
        return ticketRepository.save(ticket);
    }
}