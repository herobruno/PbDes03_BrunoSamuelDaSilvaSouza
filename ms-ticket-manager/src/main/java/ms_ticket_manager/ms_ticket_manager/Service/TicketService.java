package ms_ticket_manager.ms_ticket_manager.Service;

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
        ticket.setBrlAmount(ticketRequestDTO.getBrlAmount());
        ticket.setUsdAmount(ticketRequestDTO.getUsdAmount());
        ticket.setStatus("concluído");



        Ticket savedTicket = ticketRepository.save(ticket);

        // Criar o DTO de resposta
        TicketResponseDTO ticketResponseDTO = new TicketResponseDTO();
        ticketResponseDTO.setTicketId(savedTicket.getTicketId());
        ticketResponseDTO.setCustomerName(savedTicket.getCustomerName());
        ticketResponseDTO.setCpf(savedTicket.getCpf());
        ticketResponseDTO.setCustomerMail(savedTicket.getCustomerMail());
        ticketResponseDTO.setEventId(savedTicket.getEventId());
        ticketResponseDTO.setEventName(savedTicket.getEventName());

        // Processar a data do evento
        String eventDateTimeString = eventResponseDTO.getEventDateTime();
        try {
            if (eventDateTimeString != null && !eventDateTimeString.isEmpty()) {
                LocalDateTime eventDateTime = LocalDateTime.parse(eventDateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                ticketResponseDTO.setEventDateTime(eventDateTime);
                log.info("Data do evento parseada com sucesso: {}", eventDateTime);
            } else {
                ticketResponseDTO.setEventDateTime(LocalDateTime.now());
                log.warn("Data do evento não fornecida. Usando a data atual como padrão.");
            }
        } catch (DateTimeParseException e) {
            log.error("Erro ao parsear a data do evento: {}. Usando a data atual como fallback.", eventDateTimeString, e);
            ticketResponseDTO.setEventDateTime(LocalDateTime.now());
        }

        ticketResponseDTO.setLogradouro(eventResponseDTO.getLogradouro());
        ticketResponseDTO.setBairro(eventResponseDTO.getBairro());

        String cidade = eventResponseDTO.getCidade() != null ? eventResponseDTO.getCidade() : "Cidade não informada";
        String uf = eventResponseDTO.getUf() != null ? eventResponseDTO.getUf() : "UF não informada";
        ticketResponseDTO.setCidade(cidade);
        ticketResponseDTO.setUf(uf);

        log.info("Cidade definida no TicketResponseDTO: {}", cidade);
        log.info("UF definida no TicketResponseDTO: {}", uf);

        ticketResponseDTO.setStatus("concluído");
        ticketResponseDTO.setBrlTotalAmount(ticketRequestDTO.getBrlAmount());
        ticketResponseDTO.setUsdTotalAmount(ticketRequestDTO.getUsdAmount());

        log.info("TicketResponseDTO criado com sucesso: {}", ticketResponseDTO);

        log.info(">>> Fim do método createTicket <<<");
        return ticketResponseDTO;
    }
}
