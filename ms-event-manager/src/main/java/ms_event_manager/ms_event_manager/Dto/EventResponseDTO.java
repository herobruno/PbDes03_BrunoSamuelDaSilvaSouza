package ms_event_manager.ms_event_manager.Dto;



import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventResponseDTO {

    private String id;
    private String eventName;
    private LocalDateTime dateTime;
    private String cep;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;



}
