package ms_ticket_manager.ms_ticket_manager.Dto;

import lombok.Data;

@Data
public class EventResponseDTO {
    private String eventId;
    private String eventName;
    private String DateTime;
    private String logradouro;
    private String bairro;
    private String uf;
    private String status;
    private String localidade;
}