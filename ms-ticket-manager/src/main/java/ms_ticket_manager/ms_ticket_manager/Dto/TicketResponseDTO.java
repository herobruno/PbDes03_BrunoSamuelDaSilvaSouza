package ms_ticket_manager.ms_ticket_manager.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TicketResponseDTO {

        private String ticketId;
        private String customerName;
        private String cpf;
        private String customerMail;
        private String eventId;
        private String eventName;

        @JsonProperty("BRLtotalAmount")
        private String brlTotalAmount;

        @JsonProperty("USDtotalAmount")
        private String usdTotalAmount;

        private String status;
}
