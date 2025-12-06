package nijat.project.appointment.repository;

import nijat.project.appointment.model.entity.PendingAppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PendingAppointmentRepository extends JpaRepository<PendingAppointmentEntity, UUID> {

}
