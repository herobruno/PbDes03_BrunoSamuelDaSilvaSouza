package ms_ticket_manager.ms_ticket_manager.Entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;

@Entity
@Document(collection = "ticket")
@Data
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String ticketId;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String customerMail;

    @Column(nullable = false)
    private String eventId;

    @Column(nullable = false)
    private String eventName;

    @Column(nullable = false)
    private String BRLtotalAmoun;

    @Column(nullable = false)
    private String USDtotalAmount;

    private String status;

    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;

    private LocalDateTime dateTime;
    @Getter
    @Setter
    @javax.persistence.Id
    private Long id;


}