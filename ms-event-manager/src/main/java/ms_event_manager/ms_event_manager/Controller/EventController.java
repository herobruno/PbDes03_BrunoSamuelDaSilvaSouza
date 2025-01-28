package ms_event_manager.ms_event_manager.Controller;

import jakarta.validation.Valid;
import ms_event_manager.ms_event_manager.Dto.EventRequestDTO;
import ms_event_manager.ms_event_manager.Dto.EventResponseDTO;
import ms_event_manager.ms_event_manager.Dto.EventUpdateDTO;
import ms_event_manager.ms_event_manager.Exception.EventNotFoundException;
import ms_event_manager.ms_event_manager.Service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/create-event")
    public ResponseEntity<EventResponseDTO> createEvent(@Valid @RequestBody EventRequestDTO requestDTO) {
        EventResponseDTO responseDTO = eventService.createEvent(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/get-event/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable String id) {
        EventResponseDTO responseDTO = eventService.getEventById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/get-all-events")
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        List<EventResponseDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }
    @GetMapping("/get-all-events/sorted")
    public ResponseEntity<List<EventResponseDTO>> getAllEventsSorted() {
        List<EventResponseDTO> events = eventService.getAllEventsSorted();
        return ResponseEntity.ok(events);
    }
    @PutMapping("/update-event/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable String id,
                                                        @RequestBody EventUpdateDTO eventUpdateDTO) {
        EventResponseDTO updatedEvent = eventService.updateEvent(id, eventUpdateDTO);
        return ResponseEntity.ok(updatedEvent);
    }
    @DeleteMapping("/delete-event/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable String id) {
        try {
            if (!eventService.eventExists(id)) {
                throw new EventNotFoundException("Evento não encontrado");
            }
            eventService.deleteEvent(id);
            return ResponseEntity.noContent().build();
        } catch (EventNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ingressos vendidos para este evento");
        }
    }
}