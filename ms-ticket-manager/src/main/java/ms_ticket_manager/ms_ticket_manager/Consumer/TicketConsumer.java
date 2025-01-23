package ms_ticket_manager.ms_ticket_manager.Consumer;

import ms_ticket_manager.ms_ticket_manager.Config.RabbitMQConfig;
import ms_ticket_manager.ms_ticket_manager.Dto.TicketResponseDTO;
import ms_ticket_manager.ms_ticket_manager.Service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TicketConsumer {

    private final EmailService emailService;

    public TicketConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void processTicketConfirmation(TicketResponseDTO ticketResponseDTO) {
        try {
            String subject = "Confirmação de Ingresso: " + ticketResponseDTO.getEventName();
            String body = "Olá, " + ticketResponseDTO.getCustomerName() + ",<br><br>" +
                    "Seu ingresso para o evento <b>" + ticketResponseDTO.getEventName() + "</b> foi confirmado!<br>" +
                    "Detalhes do evento:<br>" +
                    "Data: " + ticketResponseDTO.getDateTime() + "<br>" +
                    "Local: " + ticketResponseDTO.getLogradouro() + ", " + ticketResponseDTO.getBairro() + ", " + ticketResponseDTO.getLocalidade() + " - " + ticketResponseDTO.getUf() + "<br>" +
                    "Valor Total: " + ticketResponseDTO.getBrlTotalAmount() + "<br><br>" +
                    "Obrigado por escolher nossos serviços!";

            emailService.sendEmail(ticketResponseDTO.getCustomerMail(), subject, body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}