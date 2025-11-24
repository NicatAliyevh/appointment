package nijat.project.appointment.model.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@JsonPropertyOrder({ "id", "doctorId", "patientId", "appointmentDate", "appointmentTime", "status" })
@Builder
public class AppointmentResponseDto {
    UUID id;
    UUID doctorId;
    UUID patientId;
    LocalDate appointmentDate;
    LocalTime appointmentTime;
    AppointmentStatus status;
}
