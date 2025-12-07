package nijat.project.appointment.repository;

import nijat.project.appointment.model.entity.AppointmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, UUID> {
    Page<AppointmentEntity> findAllByDoctorId(UUID doctorId, Pageable pageable);
    Optional<AppointmentEntity> findByDoctorIdAndId(UUID doctorId, UUID id);
    Page<AppointmentEntity> findAllByPatientId(UUID patientId, Pageable pageable);
    Optional<AppointmentEntity> findByPatientIdAndId(UUID patientId, UUID id);
}
