package nijat.project.appointment.model.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordChangeRequestDto {
    @Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters long.")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_\\-+=\\[\\]{};:'\",.<>?/\\\\|`~])(?=\\S+$)[A-Za-z0-9!@#$%^&*()_\\-+=\\[\\]{};:'\",.<>?/\\\\|`~]{8,30}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character."
    )
    String oldPassword;
    @Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters long.")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_\\-+=\\[\\]{};:'\",.<>?/\\\\|`~])(?=\\S+$)[A-Za-z0-9!@#$%^&*()_\\-+=\\[\\]{};:'\",.<>?/\\\\|`~]{8,30}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character."
    )
    String newPassword;
}
