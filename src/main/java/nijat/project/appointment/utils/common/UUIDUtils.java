package nijat.project.appointment.utils.common;

import nijat.project.appointment.handler.exception.InvalidUUIDFormatException;
import java.util.UUID;

public class UUIDUtils {
    public static UUID parse(String uuid) {
        try{
            return UUID.fromString(uuid);
        }catch (IllegalArgumentException ex){
            throw new InvalidUUIDFormatException("This is not a valid UUID format");
        }
    }
}
