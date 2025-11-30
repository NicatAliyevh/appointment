package nijat.project.appointment.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedAppointmentActionException extends RuntimeException{
    public UnauthorizedAppointmentActionException(String message) {
        super(message);
    }
}
