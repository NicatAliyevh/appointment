package nijat.project.appointment.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProfileRequestDto {
    @NotBlank(message = "Name cannot be empty!")
    @Size(min = 3, max = 40, message = "Name must be between 3 and 40 characters long.")
    @Pattern(
            regexp = "^[a-zA-Z0-9]+$",
            message = "Username cannot contain special characters."
    )
    String username;
}
