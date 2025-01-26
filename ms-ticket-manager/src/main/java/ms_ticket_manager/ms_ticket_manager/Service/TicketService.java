package ms_ticket_manager.ms_ticket_manager.Service;

import ms_ticket_manager.ms_ticket_manager.Config.RabbitMQConfig;
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
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {
    private final RabbitTemplate rabbitTemplate;

    public TicketService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketIdGeneratorService ticketIdGeneratorService;

    public TicketResponseDTO createTicket(TicketRequestDTO ticketRequestDTO, EventResponseDTO eventResponseDTO) {

        // Gerar o ID sequencial para o novo ticket
        String generatedTicketId = ticketIdGeneratorService.generateNextTicketId();  // Gerar o ID sequencial

        // Criar o ticket com os dados do request
        Ticket ticket = new Ticket();
        ticket.setTicketId(generatedTicketId);  // Definir o ID sequencial
        ticket.setCustomerName(ticketRequestDTO.getCustomerName());
        ticket.setCpf(ticketRequestDTO.getCpf());
        ticket.setCustomerMail(ticketRequestDTO.getCustomerMail());
        ticket.setEventId(ticketRequestDTO.getEventId());
        ticket.setEventName(ticketRequestDTO.getEventName());
        ticket.setBRLtotalAmoun(ticketRequestDTO.getBrlAmount());
        ticket.setUSDtotalAmount(ticketRequestDTO.getUsdAmount());
        ticket.setStatus("concluído");

        // Converter e definir o horário do evento
        String eventDateTimeString = eventResponseDTO.getDateTime();
        try {
            if (eventDateTimeString != null && !eventDateTimeString.isEmpty()) {
                LocalDateTime eventDateTime = LocalDateTime.parse(eventDateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                ticket.setDateTime(eventDateTime);
            } else {
                ticket.setDateTime(LocalDateTime.now());
            }
        } catch (DateTimeParseException e) {
            ticket.setDateTime(LocalDateTime.now());
        }

        // Definir o endereço do evento
        ticket.setLogradouro(eventResponseDTO.getLogradouro());
        ticket.setBairro(eventResponseDTO.getBairro());
        ticket.setLocalidade(eventResponseDTO.getLocalidade() != null ? eventResponseDTO.getLocalidade() : "Cidade não informada");
        ticket.setUf(eventResponseDTO.getUf() != null ? eventResponseDTO.getUf() : "UF não informada");

        // Salvar o ticket no banco de dados
        Ticket savedTicket = ticketRepository.save(ticket);

        // Converter o ticket salvo para DTO
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

        // Enviar a resposta via RabbitMQ
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                ticketResponseDTO
        );

        // Retornar o ticket criado
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
    public Ticket cancelTicket(String id) throws TicketNotFoundException {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket não encontrado com o ID: " + id));
        ticket.setStatus("cancelado");
        ticketRepository.save(ticket);
        return ticket;
    }
    public void cancelTicketsByCpf(String cpf) throws TicketNotFoundException {
        List<Ticket> tickets = ticketRepository.findByCpf(cpf);
        if (tickets.isEmpty()) {
            throw new TicketNotFoundException("Nenhum ingresso encontrado para o CPF: " + cpf);
        }
        tickets.forEach(ticket -> ticket.setStatus("cancelado"));
        ticketRepository.saveAll(tickets);
    }
    public List<TicketResponseDTO> getTicketsByCpf(String cpf) throws TicketNotFoundException {
        List<Ticket> tickets = ticketRepository.findByCpf(cpf);
        if (tickets.isEmpty()) {
            throw new TicketNotFoundException("Nenhum ingresso encontrado para o CPF informado: " + cpf);
        }
        List<TicketResponseDTO> ticketResponseDTOs = tickets.stream()
                .map(ticket -> {
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
                })
                .collect(Collectors.toList());

        return ticketResponseDTOs;
    }
    public List<TicketResponseDTO> getTicketsByEventId(String eventId) throws TicketNotFoundException {
        List<Ticket> tickets = ticketRepository.findByEventId(eventId);

        if (tickets.isEmpty()) {
            throw new TicketNotFoundException("Nenhum ingresso encontrado para o evento com ID: " + eventId);
        }

        List<TicketResponseDTO> ticketResponseDTOs = tickets.stream()
                .map(ticket -> {
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
                })
                .collect(Collectors.toList());

        return ticketResponseDTOs;
    }
}