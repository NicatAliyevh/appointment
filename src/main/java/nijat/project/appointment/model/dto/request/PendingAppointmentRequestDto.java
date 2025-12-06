package nijat.project.appointment.model.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PendingAppointmentRequestDto {
    @NotNull(message = "Doctor id cannot be null.")
    UUID doctorId;
    @NotNull(message = "Patient id cannot be null.")
    UUID patientId;
    @NotNull(message = "Appointment date cannot be null.")
    @Future(message = "Appointment date must be in the future.")
    LocalDate appointmentDate;
    @NotNull(message = "Appointment time cannot be null.")
    LocalTime appointmentTime;
}
