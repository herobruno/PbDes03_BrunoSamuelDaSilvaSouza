package ms_ticket_manager.ms_ticket_manager.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class TicketResponseDTO implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        private String ticketId;
        private String customerName;
        private String cpf;
        private String customerMail;
        private String eventId;
        private String eventName;
        private LocalDateTime DateTime;
        private String logradouro;
        private String bairro;
        private String localidade;
        private String uf;
        private String status;
        @JsonProperty("BRLtotalAmount")
        private String brlTotalAmount;
        @JsonProperty("USDtotalAmount")
        private String usdTotalAmount;
}



