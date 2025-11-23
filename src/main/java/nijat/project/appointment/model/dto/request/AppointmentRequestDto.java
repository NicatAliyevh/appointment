package nijat.project.appointment.model.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class AppointmentRequestDto {
    UUID doctorId;
    UUID patientId;
    LocalDate appointmentDate;
    LocalTime appointmentTime;
}
