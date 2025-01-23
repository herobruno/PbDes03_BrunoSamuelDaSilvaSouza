package ms_event_manager.ms_event_manager.Exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String message) {
        super(message);
    }
}