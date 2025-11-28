package nijat.project.appointment.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import nijat.project.appointment.model.enums.UserRole;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRegisterRequestDto {
    @NotBlank(message = "Name cannot be empty!")
    @Size(min = 3, max = 40, message = "Name must be between 3 and 40 characters long.")
    @Pattern(
            regexp = "^[a-zA-Z0-9]+$",
            message = "Username cannot contain special characters."
    )
    String username;
    @Email(message = "Email is not valid!")
    @NotBlank(message = "Email cannot be empty!")
    String email;
    @Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters long.")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_\\-+=\\[\\]{};:'\",.<>?/\\\\|`~])(?=\\S+$)[A-Za-z0-9!@#$%^&*()_\\-+=\\[\\]{};:'\",.<>?/\\\\|`~]{8,30}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character."
    )
    String password;
    UserRole role;
}
