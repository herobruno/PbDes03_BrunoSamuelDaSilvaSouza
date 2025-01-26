package ms_event_manager.ms_event_manager.Repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-ticket-manager", url = "http://localhost:8080/api")
public interface TicketManagerClient {
    @GetMapping("/check-tickets-by-event/{eventId}")
    boolean checkTicketsByEvent(@PathVariable("eventId") String eventId);
}

