package ms_ticket_manager.ms_ticket_manager.Exception;

public class TicketNotFoundException extends RuntimeException {


    public TicketNotFoundException(String message) {
        super(message);
    }

    public TicketNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}