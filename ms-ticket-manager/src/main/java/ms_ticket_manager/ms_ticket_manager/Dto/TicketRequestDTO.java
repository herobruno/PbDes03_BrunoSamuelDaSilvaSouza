package ms_ticket_manager.ms_ticket_manager.Dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TicketRequestDTO {
        private String customerName;
        private String cpf;
        private String customerMail;
        private String eventId;
        private String eventName;
        private String status;
    @JsonProperty("BRLamount")
    private String brlAmount;

    @Override
    public String toString() {
        return "TicketRequestDTO{" +
                "customerName='" + customerName + '\'' +
                ", cpf='" + cpf + '\'' +
                ", customerMail='" + customerMail + '\'' +
                ", eventId='" + eventId + '\'' +
                ", eventName='" + eventName + '\'' +
                ", status='" + status + '\'' +
                ", brlAmount='" + brlAmount + '\'' +
                ", usdAmount='" + usdAmount + '\'' +
                '}';
    }

    @JsonProperty("USDamount")
    private String usdAmount;

}


