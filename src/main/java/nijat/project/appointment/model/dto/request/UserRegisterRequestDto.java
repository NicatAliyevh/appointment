package nijat.project.appointment.model.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRegisterRequestDto {
    String username;
    String email;
    String password;
}
