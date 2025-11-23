package nijat.project.appointment.model.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import nijat.project.appointment.model.enums.AppointmentStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AppointmentResponseDto {
    UUID id;
    UUID patientId;
    UUID doctorId;
    LocalDate appointmentDate;
    LocalTime appointmentTime;
    AppointmentStatus status;
}
