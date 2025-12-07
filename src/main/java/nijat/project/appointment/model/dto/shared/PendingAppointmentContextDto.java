package nijat.project.appointment.model.dto.shared;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import nijat.project.appointment.model.entity.PendingAppointmentEntity;
import nijat.project.appointment.model.entity.UserEntity;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PendingAppointmentContextDto {
    UserEntity doctor;
    UserEntity patient;
    PendingAppointmentEntity pendingAppointment;
}
