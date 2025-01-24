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


    public EventResponseDTO(String id, String eventName, LocalDateTime dateTime, String cep, String logradouro, String bairro, String localidade, String uf) {
        this.id = id;
        this.eventName = eventName;
        this.dateTime = dateTime;
        this.cep = cep;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.localidade = localidade;
        this.uf = uf;
    }

    public EventResponseDTO() {

    }
}
