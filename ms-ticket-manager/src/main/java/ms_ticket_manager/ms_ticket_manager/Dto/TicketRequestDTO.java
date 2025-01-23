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

    @JsonProperty("BRLamount")
    private String brlAmount;

    @JsonProperty("USDamount")
    private String usdAmount;

}