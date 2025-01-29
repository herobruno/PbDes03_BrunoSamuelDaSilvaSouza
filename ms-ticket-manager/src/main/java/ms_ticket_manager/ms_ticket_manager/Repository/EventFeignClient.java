package ms_ticket_manager.ms_ticket_manager.Repository;

import ms_ticket_manager.ms_ticket_manager.Dto.EventResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "event-service", url = "http://localhost:8081/api")
public interface EventFeignClient {
    @GetMapping("/get-event/{id}")
    EventResponseDTO getEventById(@PathVariable("id") String id);

}
