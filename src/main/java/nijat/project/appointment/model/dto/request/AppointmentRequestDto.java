package nijat.project.appointment.model.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class AppointmentRequestDto {
    @NotNull(message = "Doctor ID cannot be null")
    UUID doctorId;
    @NotNull(message = "Patient ID cannot be null")
    UUID patientId;
    @NotNull(message = "Appointment date cannot be null")
    @Future(message = "Appointment date must be in the future")
    LocalDate appointmentDate;
    @NotNull(message = "Appointment time cannot be null")
    LocalTime appointmentTime;
}
