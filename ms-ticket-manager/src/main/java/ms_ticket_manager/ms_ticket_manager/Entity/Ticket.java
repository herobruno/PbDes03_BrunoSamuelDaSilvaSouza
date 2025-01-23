package ms_ticket_manager.ms_ticket_manager.Entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ticket")
@Data
public class Ticket {

    @Id
    private String ticketId;

    private String customerName;
    private String cpf;
    private String customerMail;
    private String eventId;
    private String eventName;

    private String brlAmount;
    private String usdAmount;

    private String status;
}