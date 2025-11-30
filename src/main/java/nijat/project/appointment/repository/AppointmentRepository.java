package nijat.project.appointment.repository;

import nijat.project.appointment.model.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, UUID> {
    List<AppointmentEntity> findAllByDoctorId(UUID doctorId);
    Optional<AppointmentEntity> findByDoctorIdAndId(UUID doctorId, UUID id);
    List<AppointmentEntity> findAllByPatientId(UUID patientId);
    Optional<AppointmentEntity> findByPatientIdAndId(UUID patientId, UUID id);
}
